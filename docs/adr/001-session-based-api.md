# ADR 001 — Session-Based Conversation Manager API

**Status:** Accepted  
**Date:** 2026-03-11  
**Author:** Jeff Li  
**Deciders:** Jeff Li  
**Companion Docs:** [HLD](../hld.md) · [DDD](../ddd.md) · [White Paper](../whitepaper.md)

---

## Context

The initial `ConversationManager` API design treated each manager instance as a **single-session** object — one manager per conversation. This required callers to instantiate and lifecycle-manage one object per user/session, which is:

- Cumbersome in multi-user applications (e.g., a web service with thousands of concurrent users)
- Incompatible with shared infrastructure (e.g., a singleton Spring bean)
- Awkward for session restore — requires reconstructing a manager object from storage

Additionally, the original `getContext()` returning `List<ChatMessage>` gave callers raw message data without session metadata (token count, task states, persistence flag), forcing them to track this state separately.

---

## Decision

**`ConversationManager` is redesigned as a multi-session factory.** A single instance manages any number of named sessions. Sessions are identified by a caller-supplied `String sessionId`.

### Factory pattern — one entry point

```java
// Get or create a Conversation — auto-creates session on first call
Conversation conv = manager.getConversation("user-42");
```

`ConversationManager` is a factory/registry. Its interface is minimal: `getConversation(id)`, `sessionExists(id)`, `listSessions()`, `deleteSession(id)`, and a builder.

### `Conversation` — mutable session handle

`Conversation` is a **mutable handle** to a live session. All conversation operations belong to it:

```java
// All ops on the Conversation handle — chainable
conv.addMessage(ChatMessage.text("user", "Hello!"))
    .addMessage(ChatMessage.text("assistant", "Hi there!"));

// Get managed context (compressed if over budget) — safe to pass to LLM
List<ChatMessage> context = conv.messages();

// Task lifecycle, checkpointing, rollback — all on the Conversation
conv.markTaskComplete("task-1");
conv.createCheckpoint("before-refactor");
conv.rollback(targetSeqId);
conv.persist();
```

### `Conversation` interface — key methods

```java
public interface Conversation {
    String sessionId();
    Conversation addMessage(ChatMessage message);   // returns this, chainable
    List<ChatMessage> messages();                   // managed context for LLM
    int tokenCount();
    int tokenBudget();
    Map<String, TaskState> taskStates();
    Instant createdAt();
    Instant updatedAt();
    boolean isPersisted();
    boolean isApproachingLimit();
    List<ChatMessage> activeMessages();
    Conversation markTaskComplete(String taskId);
    Conversation createCheckpoint(String label);
    Conversation restoreCheckpoint(String checkpointId);
    Conversation rollback(long targetSeqId);
    Conversation persist();
    Conversation reload();
}
```

### Session lifecycle

Sessions are **auto-created** on the first `getConversation()` call. Sessions live in-memory and are optionally persisted via the `ChatStorage` SPI. If storage is configured, `getConversation()` transparently loads the session from storage when it is not in memory.

### Internal architecture

Internally, `ConversationManager` holds:

```java
private final ConcurrentHashMap<String, Conversation> sessions = new ConcurrentHashMap<>();
```

Each `Conversation` handle wraps its own `MemoryFunnelEngine`, which is fully independent per session. Concurrent calls for different sessions never contend. Concurrent calls for the **same** session are coordinated by the per-session `StampedLock` in `SessionContext`.

### Optional persistence

If no `ChatStorage` SPI is configured, sessions exist only in-memory. If `ChatStorage` is configured, `conv.persist()` stores the session explicitly, `conv.reload()` refreshes from storage, and `getConversation()` transparently loads sessions that are in storage but not yet in memory.

---

## Consequences

### Positive

- **Simpler for callers**: one `ConversationManager` bean/instance for the whole application.
- **Natural API**: conversation operations belong to the `Conversation` object — intuitive, discoverable, and chainable.
- **Richer handle**: `Conversation` carries token count, task states, and metadata — callers no longer track this separately.
- **Transparent session restore**: `getConversation(id)` loads from storage automatically when not in memory.
- **Framework-friendly**: works as a singleton Spring bean, Quarkus CDI bean, or plain Java singleton.
- **Minimal manager interface**: `ConversationManager` has 4 methods — factory, check, list, delete. Easy to mock/stub in tests.

### Negative / Trade-offs

- **Single manager owns all sessions**: if the manager instance is lost (e.g., JVM crash), all in-memory sessions are lost — mitigated by the optional persistence SPI and explicit `conv.persist()`.
- **Session ID management is caller's responsibility**: callers must generate and track session IDs (standard practice for web services).
- **Mutable handle requires care**: callers holding a `Conversation` reference always see the latest state — this is intentional but must be understood when sharing references across threads.

### Neutral

- The Shadow-Buffer architecture, `MemoryFunnelEngine`, `DeltaPatcher`, `TaskTracker`, `ContextBroker`, and all SPI interfaces are **unchanged** — the session-keyed API is a thin orchestration layer above them.
- All existing SPI contracts (`ChatStorage`, `Summarizer`, `TokenCounter`, etc.) are compatible with the new design without modification.

---

## Alternatives Considered

### A: Keep single-session manager, caller manages lifecycle

Rejected. Requires callers to manage a `Map<String, ConversationManager>` themselves, duplicating the same pattern in every application. Moves boilerplate to every integration.

### B: Session registry as a separate class (`ConversationRegistry`)

Considered but adds unnecessary indirection. The manager and the registry are always used together, so combining them into one interface is simpler.

### C: Return `List<ChatMessage>` from operations (keep existing `getContext()` return type)

Rejected. Callers need token count and task state alongside messages to make routing decisions (e.g., "am I near the limit?"). A rich `Conversation` object provides this with zero extra API calls.

---

## References

- [DDD Section 1.5 — Conversation](../ddd.md#15-conversation--api-return-type)
- [DDD Section 12 — ConversationManager API](../ddd.md#12-builder--configuration-api)
- [HLD — Architecture Overview](../hld.md)
- [White Paper — HAC-Flow Algorithm](../whitepaper.md)

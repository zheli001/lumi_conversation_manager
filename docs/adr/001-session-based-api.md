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

**`ConversationManager` is redesigned as a multi-session manager.** A single instance manages any number of named sessions. Sessions are identified by a caller-supplied `String sessionId`.

### Two core operations

```java
// Add a message to a session (auto-creates session on first call)
// Returns the full updated Conversation — safe to pass directly to an LLM
Conversation addMessage(String sessionId, ChatMessage message);

// Retrieve the current managed context for a session
Conversation getConversation(String sessionId);
```

### `Conversation` — new first-class domain object

```java
public record Conversation(
    String sessionId,
    List<ChatMessage> messages,     // Managed context (compressed if over budget)
    int tokenCount,
    int tokenBudget,
    Map<String, TaskState> taskStates,
    Instant createdAt,
    Instant updatedAt,
    boolean persisted               // true if ChatStorage SPI is configured
) { }
```

All `ConversationManager` operations return `Conversation` — including `markTaskComplete()`, `rollback()`, `createCheckpoint()`, and `restoreCheckpoint()`.

### Session lifecycle

Sessions are **auto-created** on the first `addMessage()` call. Explicit `createSession()` is available for pre-configuration. Sessions live in-memory and are optionally persisted via the `ChatStorage` SPI.

### Internal architecture

Internally, `ConversationManager` holds:

```java
private final ConcurrentHashMap<String, MemoryFunnelEngine> sessions = new ConcurrentHashMap<>();
```

Each `MemoryFunnelEngine` is fully independent per session. Concurrent calls for different sessions never contend. Concurrent calls for the **same** session are coordinated by the per-session `StampedLock` in `SessionContext`.

### Optional persistence

If no `ChatStorage` SPI is configured, sessions exist only in-memory and are lost when the JVM exits. If `ChatStorage` is configured, `addMessage()` persists automatically, and `loadConversation(sessionId)` restores a session from storage into memory.

---

## Consequences

### Positive

- **Simpler for callers**: one `ConversationManager` bean/instance for the whole application.
- **Richer return type**: `Conversation` carries token count, task states, and metadata — callers no longer track this separately.
- **Natural session restore**: `loadConversation(id)` brings a stored session back into memory without reconstructing a new manager.
- **Framework-friendly**: works as a singleton Spring bean, Quarkus CDI bean, or plain Java singleton.
- **Scalable**: `ConcurrentHashMap` per session means O(1) session lookup with no global lock.

### Negative / Trade-offs

- **Single manager owns all sessions**: if the manager instance is lost (e.g., JVM crash), all in-memory sessions are lost — mitigated by the optional persistence SPI and explicit `persistConversation()`.
- **Session ID management is caller's responsibility**: callers must generate and track session IDs (standard practice for web services).
- **Slightly more complex internals**: engine-per-session in a map vs. a direct engine reference.

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

# Agents Guide — Lumi Conversation Manager

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/zheli001/lumi_conversation_manager/releases)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/zheli001/lumi_conversation_manager/actions)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](README.md)

## Table of Contents
- [🚀 Quick Start](#-quick-start)
- [🤖 AI Agent Behavior](#-ai-agent-behavior)
- [🧭 Design Principles](#-design-principles)
- [💻 Source Code Standards](#-source-code-standards)
- [📁 File & Documentation Rules](#-file--documentation-rules)
- [📐 Detail Design Document Template](#-detail-design-document-template)

---

## 🚀 Quick Start

```powershell
# Build all modules
.\gradlew.bat build

# Run all tests
.\gradlew.bat test

# Run tests for a single module (e.g., brain)
.\gradlew.bat :brain:test

# Clean build outputs
.\gradlew.bat clean
```

> **Requires**: JDK 17. Set `JAVA_HOME=C:\opt\java\jdk-17.0.0.1` before running the wrapper.

---

## 🤖 AI Agent Behavior

Rules for AI-assisted development in this project.

### Session Initialization
At the start of every session, read:
1. This `Agents.md`
2. `.github/copilot-instructions.md` for architecture and build context

### Understanding Problems First
- Do not make large modifications before fully understanding the root cause.
- Avoid over-engineering or unnecessary abstractions.
- ✅ Fix the specific issue in the specific file. ❌ Don't create new managers or rewrite flows.
- **When unsure**: *"I'm not fully sure of the root cause. Should I clarify before proceeding?"*

### Context & Task Alignment
- Confirm task scope and prior constraints before executing.
- Do not extend scope or add features without approval.
- ✅ Modify only what was requested. ❌ Don't rewrite adjacent logic without instruction.
- **When scope is unclear**: *"Can you confirm the exact scope and boundaries before I continue?"*

### Code Modifications
- Prefer reusing existing files and modules over creating new ones.
- Apply small patches and verify incrementally with tests.
- Do not blindly rewrite large sections to fix a single issue.
- Attempt automatic fixes up to 3 times; report to user with full context if still failing.

### File & Terminal Hygiene
- Use `tmp/` for temporary working files; clean up when done.
- Reuse existing scripts rather than creating versioned copies (`script_v2.py`, `final_script.py`).
- Combine related commands with `&&` rather than spawning new terminals.
- Use `.\gradlew.bat` on Windows PowerShell; `./gradlew` on Unix/macOS.
- Add non-interactive flags (`--no-daemon`, `-y`) to prevent commands hanging.

---

## 🧭 Design Principles

These are MANDATORY for all architecture and design decisions.

1. **Simplicity (KISS)** — Abstract only after concrete code is reused 3+ times. Favor readable over clever.
2. **Modularization** — Each module has a focused responsibility, a clear public interface, and no hidden side effects.
3. **Loose Coupling** — Depend on interfaces, not implementations. Use dependency injection (constructor injection preferred).
4. **High Cohesion** — Related data and logic stay together. One module per bounded context.
5. **Reuse First** — Search for existing components before implementing from scratch. If a mature OSS library fits, use it and document the decision in an ADR (`doc/adr/`).
6. **Project-Oriented** — Organize by feature/vertical, not pure technical layers. Document key decisions in `doc/adr/`.

**SOLID** (concise):
- Single Responsibility · Open/Closed · Liskov Substitution · Interface Segregation · Dependency Inversion

**Architecture**:
- Clear layers (Domain → Application → Infrastructure) with thin service/adapter boundaries.
- Hexagonal/Clean patterns: core domain isolated; adapters for I/O and frameworks.
- Stateless services where possible; explicit dependency injection over framework magic.

---

## 💻 Source Code Standards

- **Logging**: Use SLF4J/Logback. Never use `System.out.println()` in committed code.
  ```java
  private static final Logger LOG = LoggerFactory.getLogger(MyClass.class);
  LOG.info("Processing input: {}", input);
  ```
- **Dependency Injection**: Constructor injection preferred over field/`@Autowired` injection.
- **Method size**: Keep methods 10–30 lines. Split large classes (>1000 lines) into focused modules.
- **Naming**: Descriptive names. `calculateCommission(price, rate)` not `c(p, r)`.
- **Tests**: JUnit 5 (`@Test`, `useJUnitPlatform()`). Unit test logic; integration test external systems. Mock dependencies in unit tests.
- **Headers**: Add copyright and license headers to all source files.
- **Javadoc**: Required on all public classes and methods.
- **Base package**: `com.lumi.conversation`

---

## 📁 File & Documentation Rules

- **One document per topic** — update existing docs rather than creating `feature_v2.md`, `final.md`, etc.
- **Intermediate files** → `tmp/` subdirectories. Scripts → `scripts/` subdirectories.
- **Source files** → keep under 1000 lines; split into focused modules.
- **Design docs** (`*_design.md`) and plan docs (`*_plan.md`) must be updated when code behavior or APIs change.
- **Author**: Use "Jeff Li" as the default author in changelogs and author fields.
- **Diagrams**: Use Mermaid for architecture, sequence, and flow diagrams.
- **Large docs**: Include a TOC near the top and badge strip below the title.

**PR / commit meta** (include in every PR):
- Summary of changes and files modified
- Design docs updated (list)
- Test results (pass/fail)

---

## 📐 Detail Design Document Template

Use this structure for all `*_design.md` files:

```
1. Overview          — purpose, scope, stakeholders
2. Background        — problem statement, current state
3. Goals & Non-Goals — explicit objectives and what is out of scope
4. Architecture      — diagram, component overview, sequence/data flow
5. Detailed Design   — class/module responsibilities, public interfaces, error handling, security, performance
6. Alternatives      — other approaches considered and trade-offs
7. Testing           — test plan and validation criteria
8. Rollout           — deployment steps, migration, rollback plan
9. Open Questions    — risks and mitigations
10. Changelog        — changes to this doc + references
```

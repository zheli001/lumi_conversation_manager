# Copilot Instructions

## Project Overview

**Lumi Conversation Manager** is a hybrid open-source/binary agent system. The open-source components (brain, interface spec, examples, docs) are fully public. Core capability modules are distributed as signed binary/WASM files and must never be replaced with source-code equivalents in PRs.

## Build, Test, and Lint

```bash
# Build all modules
./gradlew build

# Run all tests
./gradlew test

# Run tests for a single module
./gradlew :brain:test

# Compile without testing
./gradlew assemble

# Clean
./gradlew clean
```

**Java 17** is required. Set `JAVA_HOME` to a JDK 17 installation before running the wrapper.

## Architecture

```
lumi-conversation-manager/          ← root Gradle project
├─ brain/                           ← Gradle submodule: planning, memory, workflow
│   └─ src/main/java/com/lumi/conversation/brain/
├─ interface/                       ← Gradle submodule: Capability Interface Spec
│   └─ src/main/java/com/lumi/conversation/iface/
├─ examples/                        ← Gradle submodule: example workflows
│   └─ src/main/java/com/lumi/conversation/examples/
├─ docs/                            ← Documentation and security guides
└─ modules/
    ├─ official/                    ← Signed verified binary/WASM modules (Release Assets)
    └─ sandbox/                     ← User-authored or experimental modules (testing only)
```

**Module dependency graph:** `examples` → `brain` → `interface`

`tmp/` is a local working directory excluded from git via `.gitignore`.

## Module System

Each module in `modules/official/` has a `manifest.json` declaring its name, version, publisher, and permissions. The `CapabilityModule` interface in the `interface` submodule defines the contract all binary modules must implement.

## CI / Binary Verification

The `binary-verify` workflow (`.github/workflows/binary-verify.yml`) runs on every push and PR:
1. Verifies signatures of all `modules/official/*.wasm` files via `./tools/verify_sig`
2. Runs each module through `./tools/sandbox_run <file> --test`

## Contribution Conventions

- PRs are accepted **only** for: `brain/`, `interface/`, `examples/`, `docs/`
- Core binary modules (`modules/official/`) are not open to PR changes
- User-authored modules belong in `modules/sandbox/` only
- Base package: `com.lumi.conversation`
- Tests use JUnit 5 (`@Test`, `useJUnitPlatform()`)


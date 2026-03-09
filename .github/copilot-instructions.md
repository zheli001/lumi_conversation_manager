# Copilot Instructions

## Project Overview

**Lumi Agent** is a hybrid open-source/binary agent system. The open-source components (brain, interface spec, examples, docs) are fully public. Core capability modules are distributed as signed binary/WASM files and must never be replaced with source-code equivalents in PRs.

## Architecture

```
/lumi-agent
├─ /brain        # Open-source planning, memory, and workflow logic
├─ /interface    # Capability Interface Specification (open)
├─ /examples     # Example workflows and safe modules
├─ /docs         # Documentation and security guides
└─ /modules
    ├─ /official # Signed, verified binary/WASM modules (Release Assets)
    └─ /sandbox  # User-authored or experimental modules (testing only)
```

**Key architectural rule:** `brain` and `interface` are open source; `modules/official` contains signed binaries that are verified via CI — do not add or modify these files directly in PRs.

## Module System

Each module in `modules/official/` has a `manifest.json` declaring its name, version, publisher, and permissions. Example:

```json
{
  "name": "git-capability",
  "version": "1.0.2",
  "publisher": "lumi-core",
  "permissions": ["filesystem.read", "network.http"]
}
```

Modules run in a sandbox with least-privilege permissions. Custom modules go in `modules/sandbox/` and must follow the Capability Interface spec defined in `/interface`.

## CI / Binary Verification

The `binary-verify` workflow (`.github/workflows/binary-verify.yml`) runs on every push and PR:
1. Verifies signatures of all `modules/official/*.wasm` files via `./tools/verify_sig`
2. Runs each module through `./tools/sandbox_run <file> --test`

Do not bypass or modify these verification steps.

## Contribution Conventions

- PRs are accepted **only** for: `/brain`, `/interface`, `/examples`, `/docs`
- Core binary modules (`modules/official/`) are not open to PR changes
- User-authored modules belong in `modules/sandbox/` only
- Security issues should be reported via `.github/ISSUE_TEMPLATE.md`

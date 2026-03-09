# Lumi Conversation Manager

## Binary Open Source Model

- Brain / Interface are fully open source
- Core capability modules use signed Binary / WASM modules that must pass signature verification
- Modules run in a sandbox with least-privilege permissions

## Installation & Usage

1. Download the official verified Binary modules from the Release Assets
2. The runtime automatically verifies module signatures
3. Modules run securely without requiring access to source code

## Custom Module Warning

- Custom modules may be unsafe
- They may only be tested in the sandbox environment
- Must comply with the Capability Interface specification

## Directory Structure

```
lumi-conversation-manager/
├─ brain/           # Open-source planning, memory, and workflow logic
├─ interface/       # Capability Interface Specification
├─ examples/        # Example workflows and safe modules
├─ docs/            # Documentation and security guides
├─ modules/
│   ├─ official/    # Signed, verified binary/WASM modules (Release Assets)
│   └─ sandbox/     # User-authored or experimental modules (testing only)
└─ tmp/             # Temporary working files (not tracked in git)
```

## Contributing

PRs are accepted only for: `brain/`, `interface/`, `examples/`, `docs/`

Core binary modules (`modules/official/`) are not open to PR changes.
See [SECURITY.md](.github/SECURITY.md) for details.

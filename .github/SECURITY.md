# Security Policy

## Verified vs. Unverified Modules

Only modules distributed through the official Release Assets and carrying a valid
cryptographic signature are considered **verified** and safe to run directly.

**Unverified modules (including anything in `modules/sandbox/`) may be unsafe.**
They may cause data leakage, execute arbitrary operations, or compromise system
integrity. The project maintainers **do not accept any responsibility or liability**
for damage caused by running unverified or third-party modules.

See [DISCLAIMER.md](../DISCLAIMER.md) for the full disclaimer of liability.

## Accepted Contributions

PRs are accepted only for the following open-source components:

- `brain/` — planning, memory, and workflow logic (AGPL-3.0)
- `interface/` — Capability Interface Specification (AGPL-3.0)
- `examples/` — example workflows
- `docs/` — documentation

Core binary modules (`modules/official/`) **cannot** be modified via PR.

## Reporting a Vulnerability

If you discover a security vulnerability, please open an issue using the
[Security Issue Template](.github/ISSUE_TEMPLATE.md) or contact the maintainers
directly. Do not disclose security vulnerabilities publicly until they have been
addressed.

## AI Training Restriction

Use of any artifact in this repository to train machine-learning or AI systems is
prohibited under the [PolyForm No AI License](../LICENSE-NoAI).

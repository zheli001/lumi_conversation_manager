# Disclaimer — Lumi Conversation Manager

## Unverified and Third-Party Modules

**WARNING: Unverified modules may be unsafe.**

Only modules distributed through the official Release Assets and carrying a valid
cryptographic signature from the Lumi Conversation Manager project are considered
"verified". Any other module — including user-authored modules placed in
`modules/sandbox/` and any module obtained from unofficial sources — is
**unverified** and carries the following risks:

- **Data leakage**: An unverified module may read, transmit, or store sensitive data
  without your knowledge or consent.
- **Arbitrary code execution**: An unverified module may execute operations outside
  the declared permission scope.
- **System compromise**: An unverified module may attempt to escalate privileges or
  modify system state beyond the sandbox boundary.

### Official Disclaimer of Liability

The Lumi Conversation Manager project, its maintainers, contributors, and copyright
holders **do not assume any responsibility or liability** for:

1. Damage or data loss caused by running unverified or third-party modules.
2. Security incidents resulting from using modules obtained outside the official
   release channel.
3. Any consequences of modifying, repackaging, or bypassing the module signature
   verification system.

Users who choose to load unverified modules do so **entirely at their own risk**.

## No Warranty

The software is provided "as is", without warranty of any kind, express or implied,
including but not limited to the warranties of merchantability, fitness for a
particular purpose, and non-infringement. In no event shall the authors or copyright
holders be liable for any claim, damages, or other liability, whether in an action of
contract, tort, or otherwise, arising from, out of, or in connection with the software
or the use or other dealings in the software.

## AI Training Restriction

This codebase is subject to the **PolyForm No AI License** (see `LICENSE-NoAI`).
You may **not** use the source code, documentation, or any other artifacts in this
repository to train, fine-tune, or otherwise develop machine-learning or artificial-
intelligence systems, including but not limited to large language models (LLMs) and
code generation models.

## Open-Source Components

The `brain/` and `interface/` submodules are licensed under the **GNU Affero General
Public License v3.0** (AGPL-3.0). See `LICENSE` for full terms.

Core capability modules distributed as signed binary/WASM files in `modules/official/`
are proprietary and are **not** covered by the AGPL license.

# Lumi Conversation Manager GitHub Binary 开源项目骨架

## 目录结构
```
/lumi-conversation-manager
│
├─ /brain                # Open Source Brain (planning, memory, workflow)
├─ /interface            # Capability Interface Spec (open)
├─ /examples             # Example workflows, safe modules
├─ /docs                 # Documentation + Security guide
├─ /modules              # Binary / WASM modules
│   ├─ /official         # 官方 verified binary (Release Asset)
│   └─ /sandbox          # 用户自制模块 / 测试
├─ /tmp                  # Temporary working files (not tracked in git)
├─ .github
│   ├─ workflows         # CI/CD pipeline
│   ├─ ISSUE_TEMPLATE.md # 提交模块或报告安全问题
│   └─ SECURITY.md       # 安全策略说明
└─ README.md
```

---

## .github/workflows/binary-verify.yml
```yaml
name: Binary Verification

on: [push, pull_request]

jobs:
  verify-binary:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Verify module signatures
        run: |
          for f in modules/official/*.wasm; do
            ./tools/verify_sig $f
          done
      - name: Run modules in sandbox
        run: |
          for f in modules/official/*.wasm; do
            ./tools/sandbox_run $f --test
          done
```

---

## modules/official/manifest.json 示例
```json
{
  "name": "conversation-capability",
  "version": "1.0.0",
  "publisher": "lumi-conversation-manager",
  "permissions": [
    "filesystem.read",
    "network.http"
  ]
}
```

---

## README.md 示例
```markdown
# Lumi Conversation Manager

## Binary 开源模式说明

- Brain / Interface 完全开源
- 核心能力模块使用 Binary / WASM 模块，必须签名验证
- 模块在 sandbox 中运行，权限最小化

## 安装与使用

1. 下载官方 verified Binary 模块（Release Asset）
2. Runtime 自动验证模块签名
3. 模块安全运行，无需访问源码

## 自制模块警告

- 自制模块可能不安全
- 只能在 sandbox 环境测试
- 需遵循 Capability Interface 规范
```

---

## .github/SECURITY.md 示例
```markdown
# Security Policy

- 官方 verified Binary 模块才可直接运行
- 未经验证模块可能不安全，可能导致数据泄露或执行不安全操作
- 用户自制模块仅用于测试或学习
- 提交 PR 仅限接口、Brain 或文档，核心 Binary 不允许修改
- 建议使用官方提供的 CI / sandbox 测试工具
```

---

## .github/ISSUE_TEMPLATE.md 示例
```markdown
# 模块开发 / 安全问题提交模板

**类型**: [模块开发 / 安全问题 / 其他]

**描述**:
请详细说明问题或提交的模块目的

**Binary / WASM 模块信息**:
- 名称:
- 版本:
- 权限:

**安全提示**:
- 所有 Binary 模块必须签名验证
- 自制模块仅用于 sandbox 测试
```

---

## License 建议
- PolyForm NoAI：禁止 AI 训练
- GPL / AGPL 样式：开源 Brain / Interface
- 明确声明未经验证模块可能不安全，官方不承担责任


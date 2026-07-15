# 实战迭代清单

按顺序做，每个任务都补测试和 README 证据。

- [x] 定义模型端口、工具协议和最小 Agent 循环
- [x] 增加会话内存与最大步数保护
- [x] 提供规则模型和安全计算器，做到零 Key 运行
- [ ] 接入一个真实模型的 Responses/function-calling API
- [ ] 使用 JSON Schema 严格校验工具参数
- [ ] 增加 SSE 流式接口与客户端取消
- [ ] 用 PostgreSQL 持久化会话和运行轨迹
- [ ] 接入 pgvector，构建带引用的混合检索 RAG
- [ ] 建立至少 50 条 golden dataset 和自动评测流水线
- [ ] 接入 OpenTelemetry，展示一次 Agent run 的完整 trace
- [ ] 增加 prompt injection 测试、工具权限与人工审批
- [ ] 编写 Docker Compose、CI、压测和架构决策记录

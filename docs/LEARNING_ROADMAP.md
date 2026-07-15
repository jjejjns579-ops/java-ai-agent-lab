# Java 工程师的 AI Agent 深度学习路线

目标不是“会调用大模型 API”，而是能设计、实现、评测并上线一个可靠的 Agent 系统。建议每周 12～15 小时，16 周完成；每一阶段都必须留下可运行代码、测试和技术文章。

## 第 0 阶段：校准基础（第 1 周）

- Java：熟练使用 record、sealed interface、CompletableFuture、虚拟线程、HTTP Client、Jackson、JUnit 5。
- 工程：Spring Boot、Maven、Docker、GitHub Actions、基本 Linux 排障。
- 数学只补必需部分：向量、余弦相似度、概率直觉，不先陷入完整高数课程。
- 验收：能解释同步/异步/流式响应的取舍，并为一个 HTTP 服务补齐单测和集成测试。

## 第 1 阶段：LLM 应用原理（第 2～3 周）

- Token、上下文窗口、temperature、结构化输出、function calling、流式输出。
- Prompt 的角色、少样本提示、上下文污染、提示注入；不要把 prompt 当作唯一控制手段。
- 自己用 Java HttpClient 调一次模型 API，再使用 SDK；记录超时、重试、限流和费用。
- 验收：实现 `ChatModel` 真实适配器，并用契约测试替换当前规则模型。

## 第 2 阶段：Agent 核心（第 4～6 周）

- ReAct / plan-and-execute、工具 schema、状态机、短期记忆、幂等性、最大步数与超时。
- 工具执行必须有参数校验、权限边界、审计日志和错误分类。
- 在本仓库实现天气/搜索/数据库只读工具，并加入并行工具调用。
- 验收：画出状态转换；能解释什么时候不该用 Agent；故障时不会无限循环或重复产生副作用。

## 第 3 阶段：RAG（第 7～9 周）

- 文档解析、chunk、embedding、向量检索、关键词检索、混合检索、rerank、引用溯源。
- 使用 PostgreSQL + pgvector；建立离线评测集，测 Recall@K、MRR 与答案忠实度。
- 做一个“Java 项目知识库助手”：回答必须附文件与行号，证据不足时拒答。
- 验收：能用数据解释 chunk 大小、top-k 和 rerank 的选择，而不是凭感觉调参。

## 第 4 阶段：生产化（第 10～12 周）

- OpenTelemetry trace、token/延迟/费用指标、prompt 与模型版本管理。
- 熔断、退避重试、限流、缓存、会话持久化、多租户、敏感信息脱敏。
- 安全：prompt injection、工具越权、SSRF、数据泄露、人工审批点。
- 验收：Docker Compose 一键启动；压测报告；关键路径 trace；故障演练与成本看板。

## 第 5 阶段：高级协议与多 Agent（第 13～14 周）

- MCP 的 client/server、resource/tool/prompt；实现一个只读 MCP Server。
- 只有单 Agent 无法清晰完成任务时再拆多 Agent；学习路由、交接、共享状态和失败传播。
- 验收：比较单 Agent 与多 Agent 的成功率、延迟、费用，写出选型结论。

## 第 6 阶段：作品集与面试（第 15～16 周）

完成两个项目：

1. 本仓库升级版：企业知识库 + 工具调用 + 评测 + 可观测性。
2. 垂直业务 Agent：例如代码审查、客服工单或数据分析，必须命中真实业务指标。

每个项目应包含架构图、ADR、演示视频、测试报告、评测集、成本估算、安全说明和线上地址。简历用数字表述，例如“构建 150 条评测集，检索 Recall@5 从 71% 提升至 89%，P95 延迟降低 35%”。

## 求职知识地图

- 必会：Java/Spring 工程能力、LLM API、tool calling、RAG、评测、可观测性、安全边界。
- 加分：Python 能读写实验脚本、LangChain4j/Spring AI、pgvector/Elasticsearch、MCP、Kubernetes。
- 不建议优先：从零训练基础模型、复杂多 Agent 编排、只背框架 API、只做聊天 UI。
- 面试准备比例：项目深挖 40%，Java/分布式基础 30%，Agent/RAG 原理 20%，算法题 10%。

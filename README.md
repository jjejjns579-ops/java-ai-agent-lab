# Java AI Agent Lab

面向 Java 工程师的 AI Agent 学习与作品集项目。第一版不需要 API Key：内置规则模型，用来观察完整的 Agent → Tool → Observation → Answer 循环。

## 快速开始

要求：JDK 17+、Maven 3.6.3+。

```bash
mvn test
mvn spring-boot:run
```

请求示例：

```bash
curl -X POST http://localhost:8080/api/agents/chat \
  -H "Content-Type: application/json" \
  -d '{"sessionId":"demo","message":"calculate 12 * (3 + 2)"}'
```

健康检查：`GET /actuator/health`。

## 代码结构

```text
com.example.agentlab
├─ api
│  ├─ controller    HTTP 接口适配
│  ├─ dto           请求与响应对象
│  └─ error         统一异常转换
├─ application
│  ├─ service       Agent 用例编排与工具注册
│  └─ port/out      模型、存储等对外依赖接口
├─ domain
│  ├─ model         消息与模型决策
│  └─ tool          工具协议
└─ infrastructure
   ├─ model         模型接口实现
   ├─ persistence   会话存储实现
   └─ tool          具体工具实现
```

依赖方向是 `api/infrastructure → application → domain`。应用层只依赖端口接口，不依赖 DeepSeek、数据库等具体实现。

详细路线见 [docs/LEARNING_ROADMAP.md](docs/LEARNING_ROADMAP.md)，迭代任务见 [docs/BACKLOG.md](docs/BACKLOG.md)。

## 第一条重要原则

Agent 不是“套一个聊天 SDK”。它是一个受控循环：模型决定下一步，程序校验并执行工具，把结果作为 observation 交还模型，直到得到最终答案或达到安全边界。
# Java AI Agent Lab

闈㈠悜 Java 宸ョ▼甯堢殑 AI Agent 瀛︿範涓庝綔鍝侀泦椤圭洰銆傜涓€鐗堜笉闇€瑕?API Key锛氬唴缃鍒欐ā鍨嬶紝鐢ㄦ潵瑙傚療瀹屾暣鐨?Agent 鈫?Tool 鈫?Observation 鈫?Answer 寰幆銆?
## 蹇€熷紑濮?
瑕佹眰锛欽DK 21銆丮aven 3.6.3+銆?
```bash
mvn test
mvn spring-boot:run
```

鍙﹀紑缁堢锛?
```bash
curl -X POST http://localhost:8080/api/agents/chat \
  -H "Content-Type: application/json" \
  -d '{"sessionId":"demo","message":"calculate 12 * (3 + 2)"}'
```

涔熷彲浠ュ彂閫佹櫘閫氭枃鏈紝渚嬪 `hello agent`銆傚仴搴锋鏌ワ細`GET /actuator/health`銆?
## 浣犳鍦ㄥ涔犱粈涔?
```text
HTTP Request
    鈫?AgentController 鈫?AgentService 鈫?ChatModel (绔彛)
                         鈫?tool call
                    ToolRegistry 鈫?AgentTool
                         鈫?observation
                    ChatModel 鈫?final answer
```

- `domain`锛氭秷鎭€佹ā鍨嬪喅绛栥€佸伐鍏峰崗璁紝瀹屽叏涓嶄緷璧?Spring 鍜屾ā鍨嬪巶鍟嗐€?- `application`锛欰gent 鐘舵€佹満銆佹渶澶ф鏁颁繚鎶ゃ€佷細璇濊蹇嗐€?- `infrastructure`锛氬綋鍓嶇殑瑙勫垯妯″瀷銆佽绠楀櫒宸ュ叿锛涙湭鏉ョ殑 OpenAI/鏈湴妯″瀷閫傞厤鍣ㄤ篃鏀捐繖閲屻€?- `api`锛欻TTP 杈撳叆杈撳嚭涓庨敊璇竟鐣屻€?
璇︾粏璺嚎瑙?[docs/LEARNING_ROADMAP.md](docs/LEARNING_ROADMAP.md)锛岃凯浠ｄ换鍔¤ [docs/BACKLOG.md](docs/BACKLOG.md)銆?
## 绗竴鏉￠噸瑕佸師鍒?
Agent 涓嶆槸鈥滃涓€涓亰澶?SDK鈥濄€傚畠鏄竴涓彈鎺у惊鐜細妯″瀷鍐冲畾涓嬩竴姝ワ紝绋嬪簭鏍￠獙骞舵墽琛屽伐鍏凤紝鎶婄粨鏋滀綔涓?observation 浜よ繕妯″瀷锛岀洿鍒板緱鍒版渶缁堢瓟妗堟垨杈惧埌瀹夊叏杈圭晫銆?

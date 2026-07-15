package com.example.agentlab.domain.model;

import java.util.Map;

/** 模型在 Agent 每一步中只允许作出的两类决定。 */
public sealed interface ModelDecision permits ModelDecision.FinalAnswer, ModelDecision.ToolCall {
    /** 模型已经可以回复用户，Agent 循环到此结束。 */
    record FinalAnswer(String content) implements ModelDecision {}
    /** 模型需要外部能力；arguments 是按照工具 Schema 生成的参数。 */
    record ToolCall(String callId, String toolName, Map<String, Object> arguments) implements ModelDecision {}
}

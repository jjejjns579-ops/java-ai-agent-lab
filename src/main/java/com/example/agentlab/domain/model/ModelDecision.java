package com.example.agentlab.domain.model;

import java.util.Map;

public sealed interface ModelDecision permits ModelDecision.FinalAnswer, ModelDecision.ToolCall {
    record FinalAnswer(String content) implements ModelDecision {}
    record ToolCall(String callId, String toolName, Map<String, Object> arguments) implements ModelDecision {}
}

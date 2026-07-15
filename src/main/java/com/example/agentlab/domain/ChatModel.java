package com.example.agentlab.domain;

import java.util.List;

public interface ChatModel {
    ModelDecision next(List<ChatMessage> messages, List<ToolDefinition> tools);
}

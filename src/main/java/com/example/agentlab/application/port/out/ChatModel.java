package com.example.agentlab.application.port.out;

import com.example.agentlab.domain.model.ChatMessage;
import com.example.agentlab.domain.model.ModelDecision;
import com.example.agentlab.domain.tool.ToolDefinition;

import java.util.List;

public interface ChatModel {
    ModelDecision next(List<ChatMessage> messages, List<ToolDefinition> tools);
}

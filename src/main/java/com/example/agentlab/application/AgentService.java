package com.example.agentlab.application;

import com.example.agentlab.domain.ChatMessage;
import com.example.agentlab.domain.ChatModel;
import com.example.agentlab.domain.ModelDecision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentService {
    private static final int MAX_STEPS = 6;
    private final ChatModel model;
    private final ToolRegistry tools;
    private final ConversationStore conversations;

    public AgentService(ChatModel model, ToolRegistry tools, ConversationStore conversations) {
        this.model = model;
        this.tools = tools;
        this.conversations = conversations;
    }

    public AgentResult chat(String sessionId, String userMessage) {
        var messages = new ArrayList<>(conversations.load(sessionId));
        messages.add(new ChatMessage(ChatMessage.Role.USER, userMessage));

        for (int step = 1; step <= MAX_STEPS; step++) {
            var decision = model.next(List.copyOf(messages), tools.definitions());
            if (decision instanceof ModelDecision.FinalAnswer answer) {
                messages.add(new ChatMessage(ChatMessage.Role.ASSISTANT, answer.content()));
                conversations.save(sessionId, messages);
                return new AgentResult(answer.content(), step);
            }
            var call = (ModelDecision.ToolCall) decision;
            String observation = tools.execute(call.toolName(), call.arguments());
            messages.add(new ChatMessage(ChatMessage.Role.ASSISTANT,
                    "tool_call:" + call.toolName() + ":" + call.callId()));
            messages.add(new ChatMessage(ChatMessage.Role.TOOL, observation));
        }
        throw new IllegalStateException("Agent exceeded max steps: " + MAX_STEPS);
    }

    public record AgentResult(String answer, int steps) {}
}

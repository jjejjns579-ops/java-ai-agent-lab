package com.example.agentlab.application.service;

import com.example.agentlab.application.port.out.ChatModel;
import com.example.agentlab.application.port.out.ConversationStore;
import com.example.agentlab.domain.model.ChatMessage;

import com.example.agentlab.domain.model.ModelDecision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentService {
    /** 防止模型反复调用工具形成死循环，同时限制费用和延迟。 */
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
        // 模型 API 通常无状态，所以每次都要带上当前会话历史。
        var messages = new ArrayList<>(conversations.load(sessionId));
        messages.add(new ChatMessage(ChatMessage.Role.USER, userMessage));

        for (int step = 1; step <= MAX_STEPS; step++) {
            // 模型决定下一步：直接回答，或者请求调用某个工具。
            var decision = model.next(List.copyOf(messages), tools.definitions());
            if (decision instanceof ModelDecision.FinalAnswer answer) {
                // 得到最终答案才结束循环并持久化会话。
                // 把工具调用与 observation 放回上下文，下一轮模型据此组织答案。
            messages.add(new ChatMessage(ChatMessage.Role.ASSISTANT, answer.content()));
                conversations.save(sessionId, messages);
                return new AgentResult(answer.content(), step);
            }
            // 模型只产生调用意图；真正执行 Java 代码的是我们自己的工具注册表。
            var call = (ModelDecision.ToolCall) decision;
            String observation = tools.execute(call.toolName(), call.arguments());
            // 把工具调用与 observation 放回上下文，下一轮模型据此组织答案。
            messages.add(new ChatMessage(ChatMessage.Role.ASSISTANT,
                    "tool_call:" + call.toolName() + ":" + call.callId()));
            messages.add(new ChatMessage(ChatMessage.Role.TOOL, observation));
        }
        throw new IllegalStateException("Agent exceeded max steps: " + MAX_STEPS);
    }

    public record AgentResult(String answer, int steps) {}
}

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
    // 定义 Agent 最多循环 6 步，防止模型一直要求调用工具造成死循环
    private static final int MAX_STEPS = 6;

    // 定义每次请求最多读取最近 20 条历史消息，避免把所有历史都塞给模型
    private static final int RECENT_MESSAGE_LIMIT = 20;

    // ChatModel 是模型抽象，负责根据消息上下文决定下一步
    private final ChatModel model;

    // ToolRegistry 是工具注册表，负责提供工具定义和执行工具
    private final ToolRegistry tools;

    // ConversationStore 是会话记忆存储，负责读取和写入聊天记录
    private final ConversationStore conversations;

    // 构造方法，Spring 会自动把 ChatModel、ToolRegistry、ConversationStore 注入进来
    public AgentService(ChatModel model, ToolRegistry tools, ConversationStore conversations) {
        // 保存模型对象，后面调用 model.next(...)
        this.model = model;

        // 保存工具注册表，后面获取工具列表和执行工具
        this.tools = tools;

        // 保存会话存储对象，后面读写用户聊天历史
        this.conversations = conversations;
    }

    // Agent 对外的核心聊天方法
// userId 是当前登录用户 id，来自后端 token 解析
// sessionId 是当前会话 id，来自前端请求
// userMessage 是用户本次输入的内容
    public AgentResult chat(String userId, String sessionId, String userMessage) {
        // 从记忆存储里读取这个用户、这场会话最近 20 条消息
        // 然后放进 ArrayList，因为后面还要继续追加本轮的新消息
        var messages = new ArrayList<>(
                conversations.findRecentMessages(userId, sessionId, RECENT_MESSAGE_LIMIT)
        );

        // 把用户本次输入包装成一条 ChatMessage
        // 角色是 USER，内容是 userMessage
        var user = new ChatMessage(ChatMessage.Role.USER, userMessage);

        // 把用户消息加入当前上下文
        // 这样模型下一步就能看到用户刚刚说了什么
        messages.add(user);

        // 把用户消息写入会话记忆
        // 以后同一个 userId + sessionId 再进来时可以查到这条消息
        conversations.addMessage(userId, sessionId, user);

        // Agent 控制循环，最多执行 MAX_STEPS 次
        // 每一轮都让模型决定下一步：直接回答，或者调用工具
        for (int step = 1; step <= MAX_STEPS; step++) {
            // 把当前消息上下文和可用工具定义交给模型
            // List.copyOf(messages) 是为了传一个不可变副本，避免模型实现误改上下文
            var decision = model.next(List.copyOf(messages), tools.definitions());

            // 如果模型决定已经可以给最终答案
            if (decision instanceof ModelDecision.FinalAnswer answer) {
                // 把最终答案包装成 ASSISTANT 消息
                var assistant = new ChatMessage(ChatMessage.Role.ASSISTANT, answer.content());

                // 把助手答案加入当前上下文
                messages.add(assistant);

                // 把助手答案写入会话记忆
                conversations.addMessage(userId, sessionId, assistant);

                // 返回本次 Agent 调用结果
                // answer.content() 是回答内容
                // step 是用了几步得到答案
                return new AgentResult(answer.content(), step);
            }

            // 如果不是最终答案，那就认为模型要调用工具
            // 这里把 ModelDecision 转成 ToolCall
            var call = (ModelDecision.ToolCall) decision;

            // 根据模型给出的工具名和参数，执行本地 Java 工具
            // 比如 calculator 工具会计算表达式
            String observation = tools.execute(call.toolName(), call.arguments());

            // 记录一次工具调用意图
            // 这里仍然用 ASSISTANT 角色，因为工具调用请求是模型/助手发起的
            var toolCall = new ChatMessage(
                    ChatMessage.Role.ASSISTANT,
                    "tool_call:" + call.toolName() + ":" + call.callId()
            );

            // 记录工具执行后的结果
            // TOOL 角色表示这条消息来自工具返回值
            var toolResult = new ChatMessage(ChatMessage.Role.TOOL, observation);

            // 把工具调用记录加入当前上下文
            // 下一轮模型能看到自己刚才调用了什么工具
            messages.add(toolCall);

            // 把工具返回结果加入当前上下文
            // 下一轮模型能基于 observation 组织最终回答
            messages.add(toolResult);

            // 把工具调用记录写入记忆
            conversations.addMessage(userId, sessionId, toolCall);

            // 把工具结果写入记忆
            conversations.addMessage(userId, sessionId, toolResult);
        }

        // 如果循环超过 MAX_STEPS 还没有最终答案，就抛异常
        // 这是 Agent 的安全保护，防止无限循环
        throw new IllegalStateException("Agent exceeded max steps: " + MAX_STEPS);
    }

    // AgentService 返回给 Controller 的结果对象
// answer 是最终回答内容
// steps 是本次 Agent 循环用了几步
    public record AgentResult(String answer, int steps) {
    }
}

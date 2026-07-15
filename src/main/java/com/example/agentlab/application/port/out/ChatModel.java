package com.example.agentlab.application.port.out;

import com.example.agentlab.domain.model.ChatMessage;
import com.example.agentlab.domain.model.ModelDecision;
import com.example.agentlab.domain.tool.ToolDefinition;

import java.util.List;

/**
 * 应用层访问大模型的输出端口。
 * AgentService 只依赖接口，因此可以替换 DeepSeek、OpenAI 或本地模型。
 */
public interface ChatModel {
    /** 根据历史消息和工具清单，返回最终答案或工具调用请求。 */
    ModelDecision next(List<ChatMessage> messages, List<ToolDefinition> tools);
}

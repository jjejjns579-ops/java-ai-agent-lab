package com.example.agentlab.infrastructure.model;

import com.example.agentlab.application.port.out.ChatModel;
import com.example.agentlab.domain.model.ChatMessage;
import com.example.agentlab.domain.model.ModelDecision;
import com.example.agentlab.domain.tool.ToolDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 零 API Key 的教学模型。它用确定性规则模拟 function calling，
 * 便于先看懂 Agent 循环，再替换为真实 DeepSeek 适配器。
 */
@Component
public class RuleBasedChatModel implements ChatModel {
    private static final Pattern CALCULATE =
            Pattern.compile("(?i)(?:calculate|\\u8ba1\\u7b97)\\s*[:\\uff1a]?\\s*(.+)");

    @Override
    public ModelDecision next(List<ChatMessage> messages, List<ToolDefinition> tools) {
        ChatMessage last = messages.get(messages.size() - 1);

        // 工具刚返回 observation：模拟模型读取结果并生成最终答案。
        if (last.role() == ChatMessage.Role.TOOL) {
            return new ModelDecision.FinalAnswer("Result: " + last.content());
        }

        Matcher matcher = CALCULATE.matcher(last.content());
        if (matcher.find()) {
            // 只产生调用意图，计算由受控的 CalculatorTool 完成。
            return new ModelDecision.ToolCall(UUID.randomUUID().toString(), "calculator",
                    Map.of("expression", matcher.group(1).trim()));
        }

        return new ModelDecision.FinalAnswer(
                "This is the zero-API-key learning model. Try: calculate 12 * (3 + 2). " +
                "Next, implement a real ChatModel adapter.");
    }
}
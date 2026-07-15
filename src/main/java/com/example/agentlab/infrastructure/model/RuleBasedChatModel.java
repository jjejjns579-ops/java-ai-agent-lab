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

@Component
public class RuleBasedChatModel implements ChatModel {
    private static final Pattern CALCULATE = Pattern.compile("(?i)(?:calculate|\\u8ba1\\u7b97)\\s*[:\\uff1a]?\\s*(.+)");

    @Override
    public ModelDecision next(List<ChatMessage> messages, List<ToolDefinition> tools) {
        ChatMessage last = messages.get(messages.size() - 1);
        if (last.role() == ChatMessage.Role.TOOL) {
            return new ModelDecision.FinalAnswer("Result: " + last.content());
        }
        Matcher matcher = CALCULATE.matcher(last.content());
        if (matcher.find()) {
            return new ModelDecision.ToolCall(UUID.randomUUID().toString(), "calculator",
                    Map.of("expression", matcher.group(1).trim()));
        }
        return new ModelDecision.FinalAnswer(
                "This is the zero-API-key learning model. Try: calculate 12 * (3 + 2). " +
                "Next, implement a real ChatModel adapter.");
    }
}

package com.example.agentlab.application.service;

import com.example.agentlab.infrastructure.model.RuleBasedChatModel;
import com.example.agentlab.infrastructure.persistence.InMemoryConversationStore;
import com.example.agentlab.infrastructure.tool.CalculatorTool;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class AgentServiceTest {
    @Test
    void completesARealToolLoop() {
        var service = new AgentService(new RuleBasedChatModel(),
                new ToolRegistry(List.of(new CalculatorTool())), new InMemoryConversationStore());

        var result = service.chat("test-user", "test-session", "calculate 12 * (3 + 2)");

        assertThat(result.answer()).contains("60");
        assertThat(result.steps()).isEqualTo(2);
    }
}

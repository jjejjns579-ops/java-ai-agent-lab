package com.example.agentlab.application;

import com.example.agentlab.infrastructure.CalculatorTool;
import com.example.agentlab.infrastructure.RuleBasedChatModel;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class AgentServiceTest {
    @Test
    void completesARealToolLoop() {
        var service = new AgentService(new RuleBasedChatModel(),
                new ToolRegistry(List.of(new CalculatorTool())), new InMemoryConversationStore());

        var result = service.chat("test-session", "calculate 12 * (3 + 2)");

        assertThat(result.answer()).contains("60");
        assertThat(result.steps()).isEqualTo(2);
    }
}

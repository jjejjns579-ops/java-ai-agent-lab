package com.example.agentlab.infrastructure;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculatorToolTest {
    private final CalculatorTool tool = new CalculatorTool();

    @Test
    void respectsOperatorPrecedenceAndParentheses() {
        assertThat(tool.execute(Map.of("expression", "12 * (3 + 2)"))).isEqualTo("60");
    }

    @Test
    void rejectsNonArithmeticInput() {
        assertThatThrownBy(() -> tool.execute(Map.of("expression", "System.exit(0)")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

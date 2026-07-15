package com.example.agentlab.infrastructure;

import com.example.agentlab.domain.AgentTool;
import com.example.agentlab.domain.ToolDefinition;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

@Component
public class CalculatorTool implements AgentTool {
    public ToolDefinition definition() {
        return new ToolDefinition("calculator", "Evaluate basic arithmetic with +, -, *, / and parentheses",
                "{\"type\":\"object\",\"properties\":{\"expression\":{\"type\":\"string\"}},\"required\":[\"expression\"]}");
    }

    public String execute(Map<String, Object> arguments) {
        Object raw = arguments.get("expression");
        if (!(raw instanceof String expression) || expression.isBlank()) {
            throw new IllegalArgumentException("expression must be a non-empty string");
        }
        return new Parser(expression).parse().stripTrailingZeros().toPlainString();
    }

    private static final class Parser {
        private final String text;
        private int pos;
        private Parser(String text) { this.text = text.replaceAll("\\s+", ""); }

        BigDecimal parse() {
            BigDecimal value = expression();
            if (pos != text.length()) throw new IllegalArgumentException("Unexpected character at " + pos);
            return value;
        }
        private BigDecimal expression() {
            BigDecimal value = term();
            while (pos < text.length() && (text.charAt(pos) == '+' || text.charAt(pos) == '-')) {
                char op = text.charAt(pos++); BigDecimal right = term();
                value = op == '+' ? value.add(right) : value.subtract(right);
            }
            return value;
        }
        private BigDecimal term() {
            BigDecimal value = factor();
            while (pos < text.length() && (text.charAt(pos) == '*' || text.charAt(pos) == '/')) {
                char op = text.charAt(pos++); BigDecimal right = factor();
                value = op == '*' ? value.multiply(right) : value.divide(right, MathContext.DECIMAL128);
            }
            return value;
        }
        private BigDecimal factor() {
            if (pos < text.length() && text.charAt(pos) == '(') {
                pos++; BigDecimal value = expression();
                if (pos >= text.length() || text.charAt(pos++) != ')') throw new IllegalArgumentException("Missing )");
                return value;
            }
            int start = pos;
            if (pos < text.length() && (text.charAt(pos) == '+' || text.charAt(pos) == '-')) pos++;
            while (pos < text.length() && (Character.isDigit(text.charAt(pos)) || text.charAt(pos) == '.')) pos++;
            if (start == pos) throw new IllegalArgumentException("Expected number at " + pos);
            return new BigDecimal(text.substring(start, pos));
        }
    }
}

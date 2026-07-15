package com.example.agentlab.application.service;

import com.example.agentlab.domain.tool.AgentTool;
import com.example.agentlab.domain.tool.ToolDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ToolRegistry {
    private final Map<String, AgentTool> tools;

    public ToolRegistry(List<AgentTool> tools) {
        this.tools = tools.stream().collect(Collectors.toUnmodifiableMap(
                tool -> tool.definition().name(), Function.identity()));
    }

    public List<ToolDefinition> definitions() {
        return tools.values().stream().map(AgentTool::definition).toList();
    }

    public String execute(String name, Map<String, Object> arguments) {
        AgentTool tool = tools.get(name);
        if (tool == null) throw new IllegalArgumentException("Unknown tool: " + name);
        return tool.execute(arguments);
    }
}

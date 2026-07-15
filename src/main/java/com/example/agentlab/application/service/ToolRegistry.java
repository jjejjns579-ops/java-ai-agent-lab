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
        // Spring 自动注入所有工具；名称重复时启动失败，避免调用歧义。
        this.tools = tools.stream().collect(Collectors.toUnmodifiableMap(
                tool -> tool.definition().name(), Function.identity()));
    }

    public List<ToolDefinition> definitions() {
        // 只把描述和参数结构暴露给模型，不暴露 Java 实现。
        return tools.values().stream().map(AgentTool::definition).toList();
    }

    public String execute(String name, Map<String, Object> arguments) {
        // 模型给出的工具名不可信，必须先在注册表中校验。
        AgentTool tool = tools.get(name);
        if (tool == null) throw new IllegalArgumentException("Unknown tool: " + name);
        return tool.execute(arguments);
    }
}

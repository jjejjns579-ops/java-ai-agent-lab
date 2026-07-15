package com.example.agentlab.domain.tool;

import java.util.Map;

public interface AgentTool {
    ToolDefinition definition();
    String execute(Map<String, Object> arguments);
}

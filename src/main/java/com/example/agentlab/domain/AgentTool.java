package com.example.agentlab.domain;

import java.util.Map;

public interface AgentTool {
    ToolDefinition definition();
    String execute(Map<String, Object> arguments);
}

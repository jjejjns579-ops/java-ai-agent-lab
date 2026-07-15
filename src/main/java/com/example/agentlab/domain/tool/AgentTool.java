package com.example.agentlab.domain.tool;

import java.util.Map;

/** 所有 Agent 工具的统一协议：definition 给模型看，execute 在本地受控执行。 */
public interface AgentTool {
    /** 返回工具名称、用途和输入 JSON Schema。 */
    ToolDefinition definition();
    /** 校验并执行模型参数，返回可供模型阅读的 observation。 */
    String execute(Map<String, Object> arguments);
}

package com.example.agentlab.api.controller;

import com.example.agentlab.api.dto.ChatRequest;
import com.example.agentlab.api.dto.ChatResponse;
import com.example.agentlab.application.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        var result = agentService.chat(request.sessionId(), request.message());
        return new ChatResponse(request.sessionId(), result.answer(), result.steps());
    }
}

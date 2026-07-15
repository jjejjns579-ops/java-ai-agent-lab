package com.example.agentlab.api;

import com.example.agentlab.application.AgentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) { this.agentService = agentService; }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        var result = agentService.chat(request.sessionId(), request.message());
        return new ChatResponse(request.sessionId(), result.answer(), result.steps());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> badRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    public record ChatRequest(@NotBlank String sessionId, @NotBlank String message) {}
    public record ChatResponse(String sessionId, String answer, int steps) {}
    public record ErrorResponse(String error) {}
}

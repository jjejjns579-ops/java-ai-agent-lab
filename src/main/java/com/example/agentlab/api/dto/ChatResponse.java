package com.example.agentlab.api.dto;

public record ChatResponse(String sessionId, String answer, int steps) {}

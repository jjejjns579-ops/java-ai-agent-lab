package com.example.agentlab.domain.model;

public record ChatMessage(Role role, String content) {
    public enum Role {USER, ASSISTANT, TOOL}
}

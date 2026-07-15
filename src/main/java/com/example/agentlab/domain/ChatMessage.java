package com.example.agentlab.domain;

public record ChatMessage(Role role, String content) {
    public enum Role { USER, ASSISTANT, TOOL }
}

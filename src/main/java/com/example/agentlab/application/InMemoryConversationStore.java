package com.example.agentlab.application;

import com.example.agentlab.domain.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryConversationStore implements ConversationStore {
    private final ConcurrentHashMap<String, List<ChatMessage>> sessions = new ConcurrentHashMap<>();

    public List<ChatMessage> load(String sessionId) {
        return List.copyOf(sessions.getOrDefault(sessionId, List.of()));
    }

    public void save(String sessionId, List<ChatMessage> messages) {
        sessions.put(sessionId, List.copyOf(messages));
    }
}

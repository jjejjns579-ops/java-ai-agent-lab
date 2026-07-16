package com.example.agentlab.infrastructure.persistence;

import com.example.agentlab.application.port.out.ConversationStore;
import com.example.agentlab.domain.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryConversationStore implements ConversationStore {
    private final ConcurrentHashMap<ConversationKey, List<ChatMessage>> sessions = new ConcurrentHashMap<>();

    public List<ChatMessage> findRecentMessages(String userId, String sessionId, int limit) {
        var messages = sessions.getOrDefault(new ConversationKey(userId, sessionId), List.of());
        int fromIndex = Math.max(0, messages.size() - limit);
        return List.copyOf(messages.subList(fromIndex, messages.size()));
    }

    public void addMessage(String userId, String sessionId, ChatMessage message) {
        sessions.compute(new ConversationKey(userId, sessionId), (key, existing) -> {
            var messages = new ArrayList<>(existing == null ? List.<ChatMessage>of() : existing);
            messages.add(message);
            return List.copyOf(messages);
        });
    }

    private record ConversationKey(String userId, String sessionId) {}
}

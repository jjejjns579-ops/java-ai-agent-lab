package com.example.agentlab.application.port.out;

import com.example.agentlab.domain.model.ChatMessage;
import java.util.List;

public interface ConversationStore {
    List<ChatMessage> load(String sessionId);
    void save(String sessionId, List<ChatMessage> messages);
}

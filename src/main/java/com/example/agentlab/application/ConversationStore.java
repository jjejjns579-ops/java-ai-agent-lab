package com.example.agentlab.application;

import com.example.agentlab.domain.ChatMessage;
import java.util.List;

public interface ConversationStore {
    List<ChatMessage> load(String sessionId);
    void save(String sessionId, List<ChatMessage> messages);
}

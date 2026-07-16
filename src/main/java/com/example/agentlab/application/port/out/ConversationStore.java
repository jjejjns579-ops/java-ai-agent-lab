package com.example.agentlab.application.port.out;

import com.example.agentlab.domain.model.ChatMessage;

import java.util.List;

/**
 * 用户存储会话信息抽象接口
 */
public interface ConversationStore {
    /**
     * 读取用户会话记录
     * @param userId 用户id
     * @param sessionId  会话id
     * @param limit  会话条数
     * @return 会话集合
     */
    List<ChatMessage> findRecentMessages(String userId, String sessionId, int limit);

    /**
     * 插入一条会话消息
     * @param userId 用户id
     * @param sessionId 会话id
     * @param message 会话内容
     */
    void addMessage(String userId, String sessionId, ChatMessage message);
}
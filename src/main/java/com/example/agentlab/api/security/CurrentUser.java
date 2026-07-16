package com.example.agentlab.api.security;

public final class CurrentUser {
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private CurrentUser() {}

    static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    public static String getUserId() {
        String userId = USER_ID.get();
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("Current user is not available");
        }
        return userId;
    }

    static void clear() {
        USER_ID.remove();
    }
}

package com.xmut.util;

public class UserContext {
    private static final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();

    public static void setUserId(Integer userId) {
        userIdHolder.set(userId);
    }

    public static Integer getUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}

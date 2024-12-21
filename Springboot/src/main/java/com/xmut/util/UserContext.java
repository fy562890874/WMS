package com.xmut.util;

public class UserContext {
    private static final ThreadLocal<Integer> userIdContext = new ThreadLocal<>();
    private static final ThreadLocal<String> usernameContext = new ThreadLocal<>();

    public static void setUserId(Integer userId) {
        userIdContext.set(userId);
    }

    public static void setUserIdAndName(Integer userId, String username) {
        userIdContext.set(userId);
        usernameContext.set(username);
    }

    public static Integer getUserId() {
        return userIdContext.get();
    }

    public static String getUsername() {
        return usernameContext.get();
    }

    public static void clear() {
        userIdContext.remove();
        usernameContext.remove();
    }
}

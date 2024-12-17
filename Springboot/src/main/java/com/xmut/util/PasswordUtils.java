package com.xmut.util;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class PasswordUtils {

    public String encode(String rawPassword) {
        return Base64Utils.encode(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    public boolean matches(String rawPassword, String storedPassword) {
        System.out.println("待匹配的密码: " + rawPassword);
        System.out.println("存储的密码: " + storedPassword);
        return rawPassword.equals(storedPassword);
    }
}
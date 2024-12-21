package com.xmut.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base64Utils {

    private static final Logger logger = LoggerFactory.getLogger(Base64Utils.class);
    
    private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static byte[] decode(String base64) {
        logger.debug("开始解码 Base64: {}", base64);
        // 移除可能的填充字符
        base64 = base64.replace("=", "");
        int length = base64.length();
        int paddingCount = (4 - (length % 4)) % 4;
        int byteLength = (length + paddingCount) * 3 / 4;
        byte[] decodedBytes = new byte[byteLength];

        int byteIndex = 0;
        int charIndex = 0;
        int buffer = 0;
        int bits = 0;

        while (charIndex < length) {
            char c = base64.charAt(charIndex++);
            int value = BASE64_CHARS.indexOf(c);
            if (value < 0) {
                throw new IllegalArgumentException("Invalid Base64 character: " + c);
            }

            buffer = (buffer << 6) | value;
            bits += 6;

            if (bits >= 8) {
                bits -= 8;
                decodedBytes[byteIndex++] = (byte) ((buffer >> bits) & 0xFF);
            }
        }

        // 处理多余的字节
        byte[] result = new byte[byteIndex];
        System.arraycopy(decodedBytes, 0, result, 0, byteIndex);
        
        logger.debug("解码后的字节数组: {}", java.util.Arrays.toString(result));
        return result;
    }

    public static String decodeToString(String base64) {
        return new String(decode(base64), java.nio.charset.StandardCharsets.UTF_8);
    }

    public static String encode(byte[] bytes) {
        StringBuilder base64 = new StringBuilder();
        int buffer = 0;
        int bits = 0;

        for (byte b : bytes) {
            buffer = (buffer << 8) | (b & 0xFF);
            bits += 8;

            while (bits >= 6) {
                bits -= 6;
                base64.append(BASE64_CHARS.charAt((buffer >> bits) & 0x3F));
            }
        }

        if (bits > 0) {
            buffer <<= (6 - bits);
            base64.append(BASE64_CHARS.charAt(buffer & 0x3F));
        }

        while (base64.length() % 4 != 0) {
            base64.append('=');
        }

        logger.debug("编码后的 Base64 字符串: {}", base64.toString());
        return base64.toString();
    }
}

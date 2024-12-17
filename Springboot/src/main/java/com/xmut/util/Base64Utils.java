package com.xmut.util;

public class Base64Utils {

    private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static byte[] decode(String base64) {
        System.out.println("开始解码 Base64: " + base64);
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

        System.out.println("解码后的字节数组: " + java.util.Arrays.toString(decodedBytes));
        return decodedBytes;
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

        return base64.toString();
    }
}

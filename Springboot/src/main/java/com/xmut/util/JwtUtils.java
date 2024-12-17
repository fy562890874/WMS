package com.xmut.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 使用 Keys 类生成密钥
    private static final long EXPIRE_TIME = 3600 * 1000; // 1 小时

    // 生成 Token
    public String generateToken(Integer userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    // 从 Token 中获取用户 ID
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    // 验证 Token 是否过期
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
    
    // 新增刷新 Token 方法
    public String refreshToken(String token) {
        if (isTokenExpired(token)) {
            throw new RuntimeException("Token已过期，无法刷新");
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Integer userId = Integer.parseInt(claims.getSubject());
        return generateToken(userId);
    }
}
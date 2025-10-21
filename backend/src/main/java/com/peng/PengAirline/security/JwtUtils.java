package com.peng.PengAirline.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;



@Service
public class JwtUtils {

    @Value("${jwtSecreteString}")
    private String jwtSecreteString;

    private static final long EXPIRATION_TIME = 30L*24*60*60*1000; //30 days in millisec
    private SecretKey key;

    @PostConstruct
    private void init(){
        byte[] keyByte = jwtSecreteString.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyByte, "HmacSHA256");

    }

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256) // 显式指定算法
                .compact();

    }
    
    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject);

    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
    
        return claimsTFunction.
                apply(Jwts.parser().
                        verifyWith(key).
                        build().
                        parseSignedClaims(token).
                        getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}

// 基本职责
// 生成 JWT：generateToken(email)
// 解析 JWT：extractClaims(...) / getUsernameFromToken(token)
// 校验 JWT：isTokenValid(token, userDetails)（用户名匹配 + 未过期）
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



@Service//告诉 Spring：这是一个可注入的服务类，其他地方可以用 @Autowired 注入它。
public class JwtUtils {

    @Value("${jwtSecreteString}")//注入密钥
    private String jwtSecreteString;

    private static final long EXPIRATION_TIME = 30L*24*60*60*1000; //30 days in millisec
    private SecretKey key;

    //这一段会在 Spring 把 jwtSecreteString 注入完成后执行，用于把字符串密钥转换为 HMAC-SHA256 对称加密的 SecretKey 对象。
    @PostConstruct
    private void init(){
        byte[] keyByte = jwtSecreteString.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyByte, "HmacSHA256");

    }

    //生成一个 JWT Token，包含sub（subject） → 用户名或邮箱；iat（签发时间）；exp（过期时间，30 天）；使用密钥 key 进行签名。
    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256) // 显式指定算法
                .compact();

    }
    //从 JWT 的 Payload 部分取出用户名（subject 字段）。
    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject);

    }

    //解析 Token 并验证签名。拿到里面的 Claims（类似一个 Map，包含 exp、sub、iat 等字段）。
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

    //验证 Token 是否：属于当前用户；没有过期。
    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}

// 基本职责
// 生成 JWT：generateToken(email)
// 解析 JWT：extractClaims(...) / getUsernameFromToken(token)
// 校验 JWT：isTokenValid(token, userDetails)（用户名匹配 + 未过期）
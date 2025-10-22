package com.peng.PengAirline.security;

import java.io.IOException;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.peng.PengAirline.exceptions.CustomAuthenticationEntryPoint;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {//OncePerRequestFilter Spring 提供的基类，保证同一个请求只执行一次 doFilterInternal

    private final JwtUtils jwtUtils;//解析 token、取用户名、校验是否过期
    private final CustomUserDetailsService customUserDetailsService;//用邮箱/用户名从数据库加载 UserDetails（用户权限、账号状态等）
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;//当认证失败（如 token 无效）时，如何返回 401 响应

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token != null) {
            try {
                String email = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                //有 token 就解析出用户名（你这里用 email 作为 username）。之后查询数据库/缓存，加载该用户的 UserDetails（包含权限）。
                if (jwtUtils.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            } catch (JwtException e) {
                // JWT 解析错误或签名无效
                log.warn("Invalid JWT token: {}", e.getMessage());
                customAuthenticationEntryPoint.commence(request, response,
                        new BadCredentialsException("Invalid JWT token"));
                return;
            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
                customAuthenticationEntryPoint.commence(request, response,
                        new BadCredentialsException("Authentication failed"));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

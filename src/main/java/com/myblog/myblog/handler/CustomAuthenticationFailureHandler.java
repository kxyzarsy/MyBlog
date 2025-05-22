package com.myblog.myblog.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        String errorMessage = "密码错误或用户不存在";
        Throwable rootCause = exception;
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", errorMessage);

        System.out.println("========= 异常链详情 =========");
        Throwable cause = exception;
        while (cause != null) {
            System.out.println("异常类型: " + cause.getClass().getName());
            cause = cause.getCause();
        }
        // 深度遍历异常链，优先检查最深层的 UsernameNotFoundException
        while (rootCause != null) {
            if (rootCause instanceof UsernameNotFoundException) {
                errorMessage = "用户不存在";
                break;
            }
            rootCause = rootCause.getCause();
        }
        
        // 将错误信息存储到会话中// 编码并重定向
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        response.sendRedirect("/login");
    }
}
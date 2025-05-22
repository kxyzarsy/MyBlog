
package com.myblog.myblog.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(authentication);
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // RoleBasedAuthenticationSuccessHandler.java
    protected String determineTargetUrl(Authentication authentication) {
        System.out.println("=== 开始处理角色跳转 ===");

        authentication.getAuthorities().forEach(grantedAuthority ->
                System.out.println("用户权限: " + grantedAuthority.getAuthority())
        );

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        System.out.println("是否为管理员: " + isAdmin);
        System.out.println("跳转路径: " + (isAdmin ? "/admin/dashboard" : "/"));

        return isAdmin ? "/admin" : "/";
    }

}

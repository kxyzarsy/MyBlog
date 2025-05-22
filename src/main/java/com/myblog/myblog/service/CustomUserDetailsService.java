package com.myblog.myblog.service;

import com.myblog.myblog.entity.User;
import com.myblog.myblog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {  // ✅ 正确注入
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        System.out.println("[DEBUG] 正在查询用户标识: " + identifier);

        // 同时查询用户名和邮箱
        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> {
                    System.out.println("[ERROR] 用户不存在: " + identifier);
                    return new UsernameNotFoundException("用户不存在");
                });

        // 确保角色格式正确
        String role = user.getRole().toUpperCase(); // 直接使用数据库中的角色（例如 ROLE_ADMIN）
        System.out.println("[DEBUG] 用户角色: " + role);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(role) // 直接使用带前缀的角色
                .build();
    }
}
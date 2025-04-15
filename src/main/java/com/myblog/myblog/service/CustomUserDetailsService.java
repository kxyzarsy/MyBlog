package com.myblog.myblog.service;

import com.myblog.myblog.entity.User;
import com.myblog.myblog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//        @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("正在查询用户: " + username);
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
//
//        // 确保用户角色前缀正确（如ROLE_USER）
//        String role = user.getRole();
//
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .authorities("ROLE_" + role) // 手动拼接前缀
//                .build();
//    }
// CustomUserDetailsService.java
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[DEBUG] 正在查询用户: " + username); // 添加日志
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("[ERROR] 用户不存在: " + username); // 明确提示用户不存在
                    return new UsernameNotFoundException("用户不存在");
                });

        String role = user.getRole();
        System.out.println("[DEBUG] 用户角色: " + role); // 确认角色字段值
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + role) // 手动拼接前缀
                .build();

    }
}
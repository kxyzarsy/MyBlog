package com.myblog.myblog.service;

import com.myblog.myblog.entity.User;
import com.myblog.myblog.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // 新增关键方法
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在: " + username));
    }
    public void registerUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("用户名已存在");
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }
    public long countAllUsers() {
        return userRepository.count();
    }
    // UserService.java 确保正确处理密码
    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 仅更新允许修改的字段
        if (!updatedUser.getUsername().equals(existingUser.getUsername())) {
            // 用户名唯一性校验
            userRepository.findByUsername(updatedUser.getUsername())
                    .ifPresent(u -> { throw new IllegalArgumentException("用户名已存在"); });
            existingUser.setUsername(updatedUser.getUsername());
        }

        existingUser.setRole(updatedUser.getRole());

        // 处理密码（假设表单字段名为newPassword）
        String newPassword = updatedUser.getPassword(); // 需要调整表单字段映射
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(existingUser);
    }



}
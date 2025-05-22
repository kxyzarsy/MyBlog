package com.myblog.myblog.service;

import com.myblog.myblog.dto.UserCreateForm;
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
    // UserService.java 修改 updateUser 方法
    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 邮箱唯一性校验（排除自身）
        if (!updatedUser.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new IllegalArgumentException("邮箱已被其他用户使用");
            }
            existingUser.setEmail(updatedUser.getEmail()); // 更新邮箱
        }

        // 用户名唯一性校验
        if (!updatedUser.getUsername().equals(existingUser.getUsername())) {
            userRepository.findByUsername(updatedUser.getUsername())
                    .ifPresent(u -> { throw new IllegalArgumentException("用户名已存在"); });
            existingUser.setUsername(updatedUser.getUsername());
        }

        existingUser.setRole(updatedUser.getRole());

        // 处理密码（需确保前端字段名是 newPassword）
        String newPassword = updatedUser.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(existingUser);
    }
    public void createUser(UserCreateForm form) {
        if (usernameExists(form.getUsername())) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(form.getRole());
        userRepository.save(user);
    }
    // 新增方法：通过邮箱检查用户是否存在
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

}
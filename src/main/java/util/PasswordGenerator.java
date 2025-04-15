package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "bbb   "; // 替换为你的明文密码
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("明文密码: " + rawPassword);
        System.out.println("加密后密码: " + encodedPassword);
    }
}
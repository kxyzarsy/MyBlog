package com.myblog.myblog.controller;

import com.myblog.myblog.entity.User;
import com.myblog.myblog.dto.RegisterRequest; // 新增DTO类
import com.myblog.myblog.service.UserService;
import com.myblog.myblog.service.EmailService; // 新增邮件服务
import com.myblog.myblog.service.VerificationCodeService; // 新增验证码服务
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Random;

@Controller
public class AuthController {
    private final UserService userService;
    private final EmailService emailService; // 新增依赖
    private final VerificationCodeService codeService; // 新增依赖

    // 修改构造函数注入新依赖
    public AuthController(UserService userService, EmailService emailService, VerificationCodeService codeService) {
        this.userService = userService;
        this.emailService = emailService;
        this.codeService = codeService;
    }

    // -------------------- 原有代码保持不变 --------------------
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            user.setRole("USER");
            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
    // -------------------- 原有代码结束 --------------------

    // ==================== 新增邮箱验证功能 ====================

    // 发送验证码接口
    @GetMapping("/auth/send-code")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String email) {
        try {
            if (userService.existsByEmail(email)) {
                return "邮箱已注册";
            }
            String code = generateRandomCode();
            codeService.saveCode(email, code);
            emailService.sendVerificationEmail(email, code);
            return "验证码已发送至" + email;
        } catch (Exception e) {
            return "发送失败：" + e.getMessage();
        }
    }

    // 邮箱验证注册接口
    @PostMapping("/register-with-code")
    public String registerWithCode(@ModelAttribute RegisterRequest request, RedirectAttributes redirectAttributes) {
        try {
            // 验证码校验
            if (!codeService.validateCode(request.getEmail(), request.getCode())) {
                redirectAttributes.addAttribute("error", "验证码错误或已过期");
                return "redirect:/auth/register";
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole("USER");
            userService.registerUser(user);

            codeService.deleteCode(request.getEmail()); // 清除已用验证码
            return "redirect:/login?registered";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    // 生成6位随机数字验证码
    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
package com.myblog.myblog.controller;

import com.myblog.myblog.dto.UserCreateForm;
import com.myblog.myblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class AdminLoginController {

    private final UserService userService;

    public AdminLoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userForm", new UserCreateForm());
        return "admin/new-user";
    }

    @PostMapping("/create")
    public String createUser(
            @ModelAttribute("userForm") @Valid UserCreateForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/new-user";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "两次密码不一致");
            return "admin/new-user";
        }

        try {
            userService.createUser(form);
        } catch (IllegalArgumentException e) {
            // 处理用户名已存在的异常
            bindingResult.rejectValue("username", "username.exists", e.getMessage());
            return "admin/new-user";
        }

        return "redirect:/admin/users";
    }



    @GetMapping("/check-username")
    @ResponseBody
    public boolean checkUsername(@RequestParam String username) {
        return userService.usernameExists(username);
    }


}
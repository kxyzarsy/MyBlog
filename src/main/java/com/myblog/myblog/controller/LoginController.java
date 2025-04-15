package com.myblog.myblog.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        String error = (String) session.getAttribute("error");
        if (error != null) {
            model.addAttribute("errorMessage", error);
            session.removeAttribute("error");
        }
        return "login";
    }

}
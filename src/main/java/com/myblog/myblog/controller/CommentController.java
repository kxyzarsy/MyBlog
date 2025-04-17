package com.myblog.myblog.controller;

import com.myblog.myblog.dto.CommentCreateRequest;
import com.myblog.myblog.service.CommentService;
import com.myblog.myblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.myblog.myblog.entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    // CommentController.java
    @PostMapping("/comments")
    public String createComment(
            @Valid @ModelAttribute("commentForm") CommentCreateRequest request,
            Authentication authentication, // 注入 Authentication 对象
            RedirectAttributes redirectAttributes
    ) {
        // 获取当前登录用户的用户名
        String username = authentication.getName();
        // 通过用户名查询自定义 User 实体
        User user = userService.findByUsername(username);

        commentService.createComment(request, user);
        redirectAttributes.addAttribute("id", request.getArticleId());
        return "redirect:/articles/{id}";
    }

}

package com.myblog.myblog.controller;

import com.myblog.myblog.dto.CommentCreateRequest;
import com.myblog.myblog.service.CommentService;
import com.myblog.myblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.myblog.myblog.entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/comments")
    public String createComment(
            @Valid @ModelAttribute("commentForm") CommentCreateRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        // 1. 检查用户是否登录
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录再发表评论");
            return "redirect:/login";
        }

        // 2. 添加内容长度验证
        if (request.getContent() != null && request.getContent().length() > 500) {
            bindingResult.rejectValue("content", "Size", "评论内容不能超过500字");
        }

        // 3. 添加articleId验证
        if (request.getArticleId() == null) {
            bindingResult.rejectValue("articleId", "NotNull", "文章ID不能为空");
        }

        // 4. 处理验证错误
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentForm", bindingResult);
            redirectAttributes.addFlashAttribute("commentForm", request);
            return "redirect:/articles/" + (request.getArticleId() != null ? request.getArticleId() : "");
        }

        try {
            // 5. 获取当前用户并创建评论
            User user = userService.findByUsername(userDetails.getUsername());
            commentService.createComment(request, user);
            redirectAttributes.addFlashAttribute("successMessage", "评论已提交，等待审核");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "评论提交失败: " + e.getMessage());
        }

        return "redirect:/articles/" + request.getArticleId();
    }

}
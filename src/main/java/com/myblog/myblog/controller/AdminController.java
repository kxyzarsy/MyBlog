package com.myblog.myblog.controller;

import com.myblog.myblog.entity.Article;
import com.myblog.myblog.repository.ArticleRepository;
import com.myblog.myblog.service.ArticleService;
import com.myblog.myblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final ArticleService articleService;

    @Autowired
    public AdminController(
            ArticleRepository articleRepository,
            UserService userService,
            ArticleService articleService
    ) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public String manageArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findAllWithAuthor(pageable);
        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        return "admin/articles";
    }

    @PostMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "redirect:/admin/articles";
    }

    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));
        model.addAttribute("article", article);
        return "admin/edit-article"; // 确保模板路径正确
    }


    @PostMapping("/articles/update/{id}")
    public String updateArticle(
            @PathVariable Long id,
            @ModelAttribute Article updatedArticle,
            RedirectAttributes redirectAttributes) {

        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article Id:" + id));

        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setContent(updatedArticle.getContent());
        existingArticle.setStatus(updatedArticle.getStatus());

        if (existingArticle.getStatus() == Article.ArticleStatus.PUBLISHED
                && existingArticle.getPublishTime() == null) {
            existingArticle.setPublishTime(LocalDateTime.now());
        } else if (existingArticle.getStatus() == Article.ArticleStatus.DRAFT) {
            existingArticle.setPublishTime(null);
        }

        articleRepository.save(existingArticle);
        redirectAttributes.addFlashAttribute("successMessage", "文章修改成功");
        return "redirect:/admin/articles";
    }

    @GetMapping("/articles/new")
    public String showNewArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/new-article";
    }

    @PostMapping("/articles")
    public String createArticle(
            @Valid @ModelAttribute("article") Article article,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes // 添加 RedirectAttributes 用于返回错误信息
    ) {
        if (result.hasErrors()) {
            return "admin/new-article";
        }

        // 处理 userDetails 为 null 的情况
        if (userDetails == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录！");
            return "redirect:/login"; // 跳转到登录页
        }

        // 确保用户存在
        try {
            article.setAuthor(userService.findByUsername(userDetails.getUsername()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "用户不存在！");
            return "redirect:/login";
        }

        article.setPublishTime(LocalDateTime.now());
        articleService.save(article);
        return "redirect:/admin/articles";
    }

    // AdminController.java
    @GetMapping("")
    public String adminDashboard(Model model, HttpServletRequest request) {
        // 获取统计数据
        long totalArticles = articleService.countAllArticles();
        long totalUsers = userService.countAllUsers();

        // 添加到模型
        model.addAttribute("totalArticles", totalArticles);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("currentPath", request.getRequestURI());

        return "admin/admin";
    }

}
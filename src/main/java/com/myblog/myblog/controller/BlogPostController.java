package com.myblog.myblog.controller;

import com.myblog.myblog.dto.CommentCreateRequest;
import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.entity.User;
import com.myblog.myblog.entity.Article;
import com.myblog.myblog.service.ArticleService;
import com.myblog.myblog.service.CommentService;
import com.myblog.myblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class BlogPostController {
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentService commentService;

    public BlogPostController(ArticleService articleService, UserService userService,CommentService commentService ) {
        this.articleService = articleService;
        this.userService = userService; // 初始化UserService
        this.commentService = commentService;
    }

    // 保持原有搜索功能不变
    @GetMapping("/post/search")
    public String search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("currentPath", request.getRequestURI());
        Page<Article> articles = articleService.searchArticles(keyword, page, size);
        model.addAttribute("articles", articles);
        model.addAttribute("searchKeyword", keyword);
        return "index";
    }

    // 保持原有首页功能不变
    @GetMapping("/")
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("currentPath", request.getRequestURI());
        Page<Article> articles = articleService.getArticles(page, size);
        model.addAttribute("articles", articles);
        return "index";
    }

    // 优化后的文章详情处理方法
    @GetMapping("/articles/{id}")
    public String articleDetail(
            @PathVariable Long id,
            Model model
    ) {
        return articleService.findArticleById(id)
                .map(article -> {
                    model.addAttribute("article", article);
                    List<Comment> comments = commentService.getCommentsByArticleId(id);
                    model.addAttribute("comments", comments);
                    model.addAttribute("commentForm", new CommentCreateRequest());
                    return "article-detail"; // 确保返回正确的模板名称
                })
                .orElseGet(() -> {
                    // 添加日志记录（需要自行实现日志组件）
                    // logger.warn("请求的文章不存在，ID: {}", id);
                    return "redirect:/?notFound=true"; // 添加重定向参数
                });
    }

    @GetMapping("/about")
    public String aboutPage(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails // 合并参数
    ) {
        model.addAttribute("currentPath", request.getRequestURI());

        if (userDetails != null) {
            // 用户已登录逻辑
            User user = userService.findByUsername(userDetails.getUsername());
            List<Article> userArticles = articleService.findByAuthor(user);
            model.addAttribute("userArticles", userArticles);
        } else {
            // 用户未登录逻辑：添加提示信息
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("loginMessage", "请先登录以查看个人信息");
        }
        return "about";
    }
}

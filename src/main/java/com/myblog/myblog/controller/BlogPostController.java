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

    public BlogPostController(ArticleService articleService, UserService userService, CommentService commentService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
    }

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

    @GetMapping("/articles/{id}")
    public String articleDetail(@PathVariable Long id, Model model, HttpServletRequest request
    ) {
        model.addAttribute("currentPath", request.getRequestURI());

        return articleService.findArticleById(id)
                .map(article -> {
                    model.addAttribute("article", article);
                    List<Comment> comments = commentService.getCommentsByArticleId(id);
                    model.addAttribute("comments", comments);
                    model.addAttribute("commentForm", new CommentCreateRequest());
                    return "article-detail";
                })
                .orElse("redirect:/?notFound=true");
    }

    @GetMapping("/about")
    public String aboutPage(
            HttpServletRequest request,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        model.addAttribute("currentPath", request.getRequestURI());

        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername());
            List<Article> userArticles = articleService.findByAuthor(user);
            model.addAttribute("userArticles", userArticles);
        } else {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("loginMessage", "请先登录以查看个人信息");
        }
        return "about";
    }
}

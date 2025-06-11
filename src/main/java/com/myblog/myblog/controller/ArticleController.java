package com.myblog.myblog.controller;

import com.myblog.myblog.entity.Article;
import com.myblog.myblog.exception.NotFoundException;
import com.myblog.myblog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/admin/article")
public class ArticleController {

    private final ArticleService articleService;
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
            "色情", "赌博", "毒品", "诈骗", "暴力"
    );

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 保留原有 REST 接口（非管理端功能）
    @GetMapping("/article/{id}")
    @ResponseBody
    public String getArticle(@PathVariable Long id) {
        if (id == 999) {
            throw new NotFoundException("文章不存在");
        }
        return "文章内容";
    }

    // 管理端文章分页列表（修复路由和变量重复问题）
    @GetMapping("")
    public String listArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        Page<Article> articlePage = articleService.getArticles(page - 1, size);
        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages()); // 直接使用分页结果
        return "admin/articles";
    }

    // 删除文章（修复重定向路径）
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/admin/articles"; // 修正为 /admin/articles
    }

    // 编辑文章页面（保持不变）
    @GetMapping("/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findArticleById(id)
                .orElseThrow(() -> new NotFoundException("文章不存在"));
        model.addAttribute("article", article);
        return "admin/edit-article";
    }

    // 更新文章（修复重定向路径）
    @PostMapping("/update")
    public String updateArticle(@ModelAttribute Article article) {
        articleService.updateArticle(article);
        return "redirect:/admin/articles"; // 修正为 /admin/articles
    }


        @PostMapping
        public String createArticle(@Valid Article article) {
            // 敏感词检测
            boolean hasSensitive = checkSensitiveContent(article);

            if(hasSensitive) {
                article.setStatus(Article.ArticleStatus.REJECTED);
                article.setRejectReason("包含系统拦截词汇");
            } else {
                article.setStatus(Article.ArticleStatus.PENDING_REVIEW);
            }

            articleService.saveArticle(article);
            return "redirect:/about";
        }

        private boolean checkSensitiveContent(Article article) {
            String content = article.getContent() + " " + article.getTitle();
            return SENSITIVE_WORDS.stream().anyMatch(content::contains);
        }


}
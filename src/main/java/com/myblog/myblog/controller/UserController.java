package com.myblog.myblog.controller;

import com.myblog.myblog.entity.Article; // 确保导入Article
import com.myblog.myblog.entity.User;
import com.myblog.myblog.exception.NotFoundException;
import com.myblog.myblog.repository.UserRepository;
import com.myblog.myblog.service.ArticleService;
import com.myblog.myblog.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final ArticleService articleService;
    private final UserRepository userRepository;
    private final UserService userService;

    // 仅保留一个构造方法
    public UserController(ArticleService articleService,
                          UserRepository userRepository,
                          UserService userService) { // 新增参数
        this.articleService = articleService;
        this.userRepository = userRepository;
        this.userService = userService; // 初始化userService
    }


    @GetMapping("/user/search")
    public String userDashboard(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Article> articles = articleService.getArticles(page, size);
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/search")
    public String searchArticles(
            Model model,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Article> articles = articleService.searchArticles(keyword, page, size);
        model.addAttribute("articles", articles);
        model.addAttribute("searchKeyword", keyword);
        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users") // 修正路径
    public String listUsers(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/users/delete/{id}")  // 路径与表单提交路径匹配
    public String deleteUser(@PathVariable("id") Long userId) {
        userRepository.deleteById(userId);
        return "redirect:/admin/users";
    }
    @GetMapping("/admin/users/edit/{userId}")
    public String editUserForm(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("用户不存在"));
        model.addAttribute("user", user);
        return "admin/edit-user"; // 需要创建对应的模板文件
    }

    // UserController.java 修改updateUser方法
    @PostMapping("/admin/users/update")
    public String updateUser(@ModelAttribute User updatedUser) {
        userService.updateUser(updatedUser); // 调用Service处理业务逻辑
        return "redirect:/admin/users";
    }





}

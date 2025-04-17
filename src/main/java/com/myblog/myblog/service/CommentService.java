package com.myblog.myblog.service;

import com.myblog.myblog.dto.CommentCreateRequest;
import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.myblog.myblog.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    // 新增方法：根据文章ID获取评论
    public List<Comment> getCommentsByArticleId(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreateTimeDesc(articleId);
    }

    @Transactional
    public void createComment(CommentCreateRequest request, User user) {
        Comment comment = new Comment(); // 创建Comment对象
        comment.setContent(request.getContent());
        comment.setArticle(articleService.getById(request.getArticleId())); // 设置关联文章
        comment.setUser(user); // 设置作者

        commentRepository.save(comment);
    }



}
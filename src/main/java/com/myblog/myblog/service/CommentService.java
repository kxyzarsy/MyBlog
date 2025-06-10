package com.myblog.myblog.service;

import com.myblog.myblog.dto.CommentCreateRequest;
import com.myblog.myblog.entity.Article;
import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.entity.Comment.CommentStatus;
import com.myblog.myblog.entity.User;
import com.myblog.myblog.repository.CommentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService; // 添加依赖

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          ArticleService articleService) { // 注入ArticleService
        this.commentRepository = commentRepository;
        this.articleService = articleService;
    }

    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    public Page<Comment> findByStatusAndKeyword(CommentStatus status, String keyword, Pageable pageable) {
        return commentRepository.findByStatusAndKeyword(status, keyword, pageable);
    }

    public long countAllComments() {
        return commentRepository.count();
    }

    public long countByStatus(CommentStatus status) {
        return commentRepository.countByStatus(status);
    }

    @Transactional
    public void updateStatus(Long id, CommentStatus status) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        comment.setStatus(status);
        commentRepository.save(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        try {
            commentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Invalid comment Id:" + id, e);
        }
    }

    public void createComment(CommentCreateRequest request, User user) {
        Article article = (Article) articleService.getArticleById(request.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("文章不存在"));

        Comment comment = new Comment();
        comment.setAuthor(user.getUsername());
        comment.setEmail(user.getEmail());
        comment.setContent(request.getContent());
        comment.setArticle(article);
        comment.setStatus(Comment.CommentStatus.PENDING);

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByArticleId(Long id) {
        return commentRepository.findByArticleId(id); // 修复：调用repository方法
    }

    public Page<Comment> findByStatusAndKeywordWithArticle(
            Comment.CommentStatus status,
            String keyword,
            Pageable pageable) {
        return commentRepository.findByStatusAndKeywordWithArticle(status, keyword, pageable);
    }
}

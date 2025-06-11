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
    private final ArticleService articleService;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
    }

    @Transactional
    public void createComment(CommentCreateRequest request, User user) {
        Article article = articleService.getById(request.getArticleId());

        Comment comment = new Comment();
        comment.setAuthor(user.getUsername());
        comment.setEmail(user.getEmail());
        comment.setContent(request.getContent());
        comment.setArticle(article);
        comment.setUser(user);

        // 添加敏感词检测
        if (comment.containsSensitiveWords()) {
            comment.setStatus(CommentStatus.SPAM);
        } else {
            // 默认设置为待审核状态
            comment.setStatus(CommentStatus.PENDING);
        }

        commentRepository.save(comment);
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
        // 状态转换验证
        if (status == CommentStatus.APPROVED && comment.getStatus() == CommentStatus.SPAM) {
            // 需要额外检查才能从垃圾状态恢复
            if (!comment.containsSensitiveWords()) {
                comment.setStatus(status);
            } else {
                throw new IllegalStateException("包含敏感词的评论不能批准");
            }
        } else {
            comment.setStatus(status);
        }

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

    public List<Comment> getCommentsByArticleId(Long id) {
        return commentRepository.findByArticleId(id);
    }

    public Page<Comment> findByStatusAndKeywordWithArticle(
            Comment.CommentStatus status,
            String keyword,
            Pageable pageable) {
        return commentRepository.findByStatusAndKeywordWithArticle(status, keyword, pageable);
    }


}

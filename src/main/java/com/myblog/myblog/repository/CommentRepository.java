package com.myblog.myblog.repository;

import com.myblog.myblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // ✅ 正确写法：方法声明 + 分号
    List<Comment> findByArticleIdOrderByCreateTimeDesc(Long articleId);

    // ✅ 正确写法：使用 @Query 注解
    @Query("SELECT c FROM Comment c WHERE c.article.id = :articleId ORDER BY c.createTime DESC")
    List<Comment> findByArticleCustomQuery(@Param("articleId") Long articleId);
}

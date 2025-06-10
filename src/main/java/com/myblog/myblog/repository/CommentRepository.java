package com.myblog.myblog.repository;

import com.myblog.myblog.entity.Comment;
import com.myblog.myblog.entity.Comment.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAll(Pageable pageable);

    long countByStatus(CommentStatus status);

    @Query("SELECT c FROM Comment c WHERE c.article.id = :articleId ORDER BY c.createTime DESC")
    List<Comment> findByArticleId(@Param("articleId") Long articleId);

    @Query("SELECT c FROM Comment c WHERE " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:keyword IS NULL OR c.content LIKE %:keyword%) " +
            "ORDER BY c.createTime DESC")
    Page<Comment> findByStatusAndKeyword(
            @Param("status") CommentStatus status,
            @Param("keyword") String keyword,
            Pageable pageable);
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.article WHERE " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:keyword IS NULL OR c.content LIKE %:keyword%)")
    Page<Comment> findByStatusAndKeywordWithArticle(
            @Param("status") Comment.CommentStatus status,
            @Param("keyword") String keyword,
            Pageable pageable);

}

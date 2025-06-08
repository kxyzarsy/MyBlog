package com.myblog.myblog.repository;

import com.myblog.myblog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.author WHERE a.id = :id")
    Optional<Article> findByIdWithAuthor(@Param("id") Long id);

    /**
     * 根据关键词搜索文章（忽略大小写，模糊匹配标题或内容)
     *
     * @param keyword  搜索关键词
     * @param pageable 分页参数
     */
    @Query("SELECT a FROM Article a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(CAST(a.content AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Article> searchArticles(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.author")
    List<Article> findAllWithAuthor();

    @Query(value = "SELECT a FROM Article a LEFT JOIN FETCH a.author",
            countQuery = "SELECT COUNT(a) FROM Article a")
    Page<Article> findAllWithAuthor(Pageable pageable);

    List<Article> findByAuthor(com.myblog.myblog.entity.User user);
}
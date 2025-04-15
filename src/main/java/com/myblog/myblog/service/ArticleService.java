package com.myblog.myblog.service;

import com.myblog.myblog.entity.Article;
import com.myblog.myblog.exception.ArticleNotFoundException;
import com.myblog.myblog.repository.ArticleRepository;
import com.myblog.myblog.entity.User; // 确保此导入存在
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * 分页获取所有文章
     */
    public Page<Article> getArticles(int page, int size) {
        return articleRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * 根据关键词搜索文章
     */
    public Page<Article> searchArticles(String keyword, int page, int size) {
        return articleRepository.searchArticles(keyword, PageRequest.of(page, size));
    }

    // 关键修改：返回 Optional<Article>
    public Optional<Article> findArticleById(Long id) {
        return articleRepository.findById(id); // 不再直接解包 Optional
    }

    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ArticleNotFoundException("文章不存在");
        }
        articleRepository.deleteById(id);
    }
    // 新增 save 方法
    public void save(Article article) {
        articleRepository.save(article);

    }
    public long countAllArticles() {
        return articleRepository.count();
    }

    public List<Article> findByAuthor(User user) { // 参数类型正确
        return articleRepository.findByAuthor(user);
    }
}
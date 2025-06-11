package com.myblog.myblog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class Comment {
    public enum CommentStatus {
        PENDING("待审核"),
        APPROVED("已发布"),
        SPAM("垃圾评论");

        private final String displayName;

        CommentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // 敏感词列表
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
            "色情", "赌博", "毒品", "诈骗", "暴力"
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private String email;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 关联用户实体


    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.PENDING;

    private LocalDateTime createTime = LocalDateTime.now();

    // 检测评论是否包含敏感词
    public boolean containsSensitiveWords() {
        if (content == null) return false;
        return SENSITIVE_WORDS.stream().anyMatch(content::contains);
    }

    public String getAuthorName() {
        return this.user != null ? this.user.getUsername() : "匿名用户";
    }
}

package com.myblog.myblog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    public enum CommentStatus {
        PENDING("待审核"),
        APPROVED("已批准"),
        SPAM("垃圾评论");

        private final String displayName;

        CommentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

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

    public String getAuthorName() {
        return this.user != null ? this.user.getUsername() : "匿名用户";
    }
}

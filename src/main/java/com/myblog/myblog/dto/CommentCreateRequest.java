package com.myblog.myblog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotBlank(message = "评论内容不能为空")
    private String content;

    @NotNull(message = "文章ID不能为空")
    @Min(value = 1, message = "文章ID必须大于0")
    private Long articleId;
}

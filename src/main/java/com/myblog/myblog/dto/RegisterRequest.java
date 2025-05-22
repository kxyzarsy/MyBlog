package com.myblog.myblog.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String code; // 验证码字段
}
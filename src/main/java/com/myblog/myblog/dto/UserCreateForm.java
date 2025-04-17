package com.myblog.myblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

    @Data
    public class UserCreateForm {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名必须为3-20位字符")
        private String username;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, message = "密码至少6位字符")
        private String password;

        @NotBlank(message = "确认密码不能为空")
        private String confirmPassword;

        @NotBlank(message = "角色不能为空")
        private String role;
}

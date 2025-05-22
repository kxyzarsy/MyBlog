package com.myblog.myblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateForm {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名必须为3-20位字符")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确") // 添加邮箱格式验证
    private String email; // 新增邮箱字段

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码至少6位字符")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @NotBlank(message = "角色不能为空")
    private String role;
}
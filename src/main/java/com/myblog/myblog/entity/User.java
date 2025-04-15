package com.myblog.myblog.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime; // 新增导入
import java.sql.ConnectionBuilder;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table(name = "user")
public class User implements UserDetails { // 确保实现 UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // 对应表中的 user_id 列
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")     // 对应表中的 role 列
    private String role;



    @PrePersist // 新增的JPA生命周期回调方法
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public static ElasticsearchConnectionDetails.Node builder() {
        return null;
    }

    public static ConnectionBuilder withUsername(String username) {
        return null;
    }

    // 必须实现 UserDetails 接口的所有方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }


}
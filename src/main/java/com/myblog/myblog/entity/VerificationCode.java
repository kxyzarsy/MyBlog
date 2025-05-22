package com.myblog.myblog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verification_code")
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String code;
    private LocalDateTime expireTime;

    public VerificationCode() {}

    public VerificationCode(String email, String code, LocalDateTime expireTime) {
        this.email = email;
        this.code = code;
        this.expireTime = expireTime;
    }
}
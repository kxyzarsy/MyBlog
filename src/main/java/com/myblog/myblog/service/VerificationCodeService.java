package com.myblog.myblog.service;

public interface VerificationCodeService {
    void saveCode(String email, String code);
    boolean validateCode(String email, String code);
    void deleteCode(String email);
}
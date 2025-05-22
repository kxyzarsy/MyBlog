package com.myblog.myblog.service;

import com.myblog.myblog.entity.VerificationCode;
import com.myblog.myblog.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final VerificationCodeRepository codeRepository;

    public VerificationCodeServiceImpl(VerificationCodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Override
    public void saveCode(String email, String code) {
        // 保存验证码到数据库（需实现 VerificationCodeRepository）
        codeRepository.save(new VerificationCode(email, code, LocalDateTime.now().plusMinutes(5)));
    }

    @Override
    public boolean validateCode(String email, String code) {
        VerificationCode vCode = codeRepository.findByEmailAndCode(email, code)
                .orElse(null);
        return vCode != null && vCode.getExpireTime().isAfter(LocalDateTime.now());
    }

    @Override
    public void deleteCode(String email) {
        codeRepository.deleteByEmail(email);
    }
}
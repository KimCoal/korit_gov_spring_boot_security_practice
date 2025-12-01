package com.korit.security_practice.repository;

import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.mapper.VerifyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VerifyRepository {
    private final VerifyMapper verifyMapper;

    public void addVerify(Verify verify) {
        verifyMapper.addVerify(verify);
    }

    public Optional<Verify> findVerifyByUserId(Integer userId) {
        return verifyMapper.findVerifyByUserId(userId);
    }

    public int deleteVerifyByUserId (Integer userId) {
        return verifyMapper.deleteVerifyByUserId(userId);
    }
}

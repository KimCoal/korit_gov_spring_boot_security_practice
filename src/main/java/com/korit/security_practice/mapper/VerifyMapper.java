package com.korit.security_practice.mapper;

import com.korit.security_practice.entity.Verify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface VerifyMapper {
    void addVerify(Verify verify);
    Optional<Verify> findVerifyByUserId(Integer userId);
    int deleteVerifyByUserId(Integer userId);
}

package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.UserRoleRepository;
import com.korit.security_practice.repository.VerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyService {

    private final VerifyRepository verifyRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public ApiRespDto<?> getVerifyCode(Integer userId) {

        Optional<Verify> foundVerify = verifyRepository.findVerifyByUserId(userId);
        System.out.println("principal" + userId);

        if (foundVerify.isEmpty()) {
            return new ApiRespDto<>("failed", "인증코드가 존재하지 않습니다.", null);
        }

        return new ApiRespDto<>("success", "인증코드 조회 성공", foundVerify.get().getVerifyCode());
    }


    public ApiRespDto<?> verifyUser(Integer userId, String inputCode) {

        Optional<User> foundUser = userRepository.findUserByUserId(userId);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 사용자입니다.", null);
        }

        Optional<Verify> foundVerify = verifyRepository.findVerifyByUserId(userId);
        if (foundVerify.isEmpty()) {
            return new ApiRespDto<>("failed", "인증코드가 존재하지 않습니다.", null);
        }

        Verify verify = foundVerify.get();

        if (!verify.getVerifyCode().equals(inputCode)) {
            return new ApiRespDto<>("failed", "인증코드가 일치하지 않습니다.", null);
        }

        int deleteResult = verifyRepository.deleteVerifyByUserId(userId);
        if (deleteResult != 1) {
            return new ApiRespDto<>("failed", "인증코드 삭제 중 오류 발생", null);
        }

        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(2)
                .build();

        int roleResult = userRoleRepository.modifyRole(userRole);
        if (roleResult != 1) {
            return new ApiRespDto<>("failed", "권한 변경 중 오류 발생", null);
        }

        return new ApiRespDto<>("success", "이메일 인증 완료! 일반 사용자로 승인되었습니다.", null);
    }
}
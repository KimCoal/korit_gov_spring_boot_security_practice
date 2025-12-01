package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.ModifyPasswordReqDto;
import com.korit.security_practice.dto.ModifyUsernameReqDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public ApiRespDto<?> modifyPassword(ModifyPasswordReqDto modifyPasswordReqDto, Principal principal) {
        if (!modifyPasswordReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다", modifyPasswordReqDto.getUserId());
        }
        Optional<User> foundUser = userRepository.findUserByUserId(modifyPasswordReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않은 사용자", modifyPasswordReqDto.getUserId());
        }

        if (!bCryptPasswordEncoder.matches(modifyPasswordReqDto.getOldPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "기존 비밀번호가 일치하지 않습니다", null);
        }

        if (bCryptPasswordEncoder.matches(modifyPasswordReqDto.getNewPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "새 비밀번호는 기존 비밀번호와 달라야합니다", null);
        }

        int result = userRepository.modifyPassword(modifyPasswordReqDto.toEntity(bCryptPasswordEncoder));
        if (result != 1) {
            return new ApiRespDto<>("failed", "문제가 발생", null);
        }

        return new ApiRespDto<>("success", "비밀번호 변경 성공", null);
    }

    public ApiRespDto<?> modifyUsername(ModifyUsernameReqDto modifyUsernameReqDto, Principal principal) {
        if (!modifyUsernameReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다", modifyUsernameReqDto.getUserId());
        }
        Optional<User> foundUser = userRepository.findUserByUserId(modifyUsernameReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않습니다", modifyUsernameReqDto.getUserId());
        }
        int result = userRepository.modifyUsername(modifyUsernameReqDto.toEntity());
        if (result != 1) {
            return new ApiRespDto<>("failed", "뭔가 이상함", null);
        }
        return new ApiRespDto<>("success", "캬 ㅋㅋ", null);

    }

}

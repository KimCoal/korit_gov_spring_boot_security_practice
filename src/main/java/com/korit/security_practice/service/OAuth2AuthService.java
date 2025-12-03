package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.OAuth2MergeReqDto;
import com.korit.security_practice.dto.OAuth2SignupReqDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.repository.OAuth2UserRepository;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.UserRoleRepository;
import com.korit.security_practice.repository.VerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final VerifyRepository verifyRepository;
    private final AuthService authService;
    private final OAuth2UserRepository oAuth2UserRepository;

    public ApiRespDto<?> signup (OAuth2SignupReqDto oAuth2SignupReqDto) {
        Optional<User> foundUser = userRepository.findUserByEmail(oAuth2SignupReqDto.getEmail());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 이메일입니다", null);
        }

        Optional<?> foundUsername = userRepository.findUserByUsername(oAuth2SignupReqDto.getUsername());
        if (foundUsername.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 사용자 이름", null);
        }
        Optional<User> optionalUser = userRepository.addUser(oAuth2SignupReqDto.toUserEntity(bCryptPasswordEncoder));
        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);
        Verify verify = Verify.builder()
                .userId(optionalUser.get().getUserId())
                .verifyCode(authService.generateVerifyCode())
                .build();
        verifyRepository.addVerify(verify);
        oAuth2UserRepository.addOAuth2User(oAuth2SignupReqDto.toOAuth2UserEntity(optionalUser.get().getUserId()));
        return new ApiRespDto<>("success", "가입 성공", verify.getVerifyCode());

    }
    public ApiRespDto<?> merge(OAuth2MergeReqDto oAuth2MergeReqDto) {
        Optional<User> foundUser = userRepository.findUserByEmail(oAuth2MergeReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보가 일치하지 않습니다", null);
        }
        if (!bCryptPasswordEncoder.matches(oAuth2MergeReqDto.getPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보가 일치하지 않습니다", null);
        }

        oAuth2UserRepository.addOAuth2User(oAuth2MergeReqDto.toEntity(foundUser.get().getUserId()));
        return new ApiRespDto<>("success", "회원가입 성공", null);
    }

}

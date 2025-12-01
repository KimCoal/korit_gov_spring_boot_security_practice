package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.SignInReqDto;
import com.korit.security_practice.dto.SignUpReqDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.UserRoleRepository;
import com.korit.security_practice.repository.VerifyRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import com.korit.security_practice.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;
    private final VerifyRepository verifyRepository;

    public ApiRespDto<?> addUser (SignUpReqDto signupReqDto) {
        // email 중복확인
        Optional<User> foundUser = userRepository.findUserByEmail(signupReqDto.getEmail());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 이메일", signupReqDto.getEmail());
        }
        // username 중복확인
        Optional<User> findUser = userRepository.findUserByUsername(signupReqDto.getUsername());
        if (findUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 닉네임", signupReqDto.getUsername());
        }

        // repository,addUSer
        Optional<User> optionalUser = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));

        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);

        Verify verify = Verify.builder()
                .userId(optionalUser.get().getUserId())
                .verifyCode(generateVerifyCode())
                .build();
        verifyRepository.addVerify(verify);
        return new ApiRespDto<>("success" , "가입완료" , verify);

    }

    public ApiRespDto<?> signin(SignInReqDto signinReqDto) {
        // email 조회
        Optional<User> foundUser = userRepository.findUserByEmail(signinReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 유저 없음", signinReqDto.getEmail());
        }
        User user = foundUser.get();
        if (!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "정보 확인", null);
        }

        String token = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "로그인 성공", token);
    }


    // 5자리 자동생성
    public String generateVerifyCode() {
        int num = new Random().nextInt(100000); // 0 ~ 99999
        return String.format("%05d", num);      // 5자리로 맞춤 (앞자리 0 포함)
    }
}

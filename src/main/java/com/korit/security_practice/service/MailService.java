package com.korit.security_practice.service;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.SendMailReqDto;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.entity.Verify;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.repository.UserRoleRepository;
import com.korit.security_practice.repository.VerifyRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import com.korit.security_practice.security.model.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final JwtUtils jwtUtils;

    private final JavaMailSender javaMailSender;

    private final VerifyRepository verifyRepository;

    public ApiRespDto<?> sendMail(SendMailReqDto sendMailReqDto, Principal principal) {

        Optional<User> foundUser = userRepository.findUserByEmail(sendMailReqDto.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 이메일입니다", null);
        }
        User user = foundUser.get();
        Optional<Verify> foundVerify = verifyRepository.findVerifyByUserId(user.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "인증코드가 존재하지않습니다", null);
        }

        String verifyCode = foundVerify.get().getVerifyCode();

        // 이메일 내용
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendMailReqDto.getEmail());
        message.setSubject("[이메일 인증]");
        message.setText(
                "인증코드: " + verifyCode +
                        "\n\n아래 링크에서 이메일과 인증코드를 입력하세요:\n" +
                        "http://localhost:8080/mail/verify-page"
        );

        javaMailSender.send(message);

        return new ApiRespDto<>("success", "인증메일이 전송되었습니다.", null);
    }

    public ApiRespDto<?> verifyUser(String email, String inputCode) {

        Optional<User> foundUser = userRepository.findUserByEmail(email);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 이메일입니다.", null);
        }

        User user = foundUser.get();

        Optional<Verify> foundVerify = verifyRepository.findVerifyByUserId(user.getUserId());
        if (foundVerify.isEmpty()) {
            return new ApiRespDto<>("failed", "인증코드가 존재하지 않습니다.", null);
        }

        Verify verify = foundVerify.get();

        if (!verify.getVerifyCode().equals(inputCode)) {
            return new ApiRespDto<>("failed", "인증코드가 일치하지 않습니다.", null);
        }

        // 인증코드 삭제
        verifyRepository.deleteVerifyByUserId(user.getUserId());

        // 권한 변경
        UserRole userRole = UserRole.builder()
                .userId(user.getUserId())
                .roleId(2) // 일반 유저
                .build();
        userRoleRepository.modifyRole(userRole);

        return new ApiRespDto<>("success", "이메일 인증이 완료되었습니다!", null);
    }
}

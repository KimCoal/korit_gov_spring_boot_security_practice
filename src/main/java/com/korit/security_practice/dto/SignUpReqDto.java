package com.korit.security_practice.dto;

import com.korit.security_practice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReqDto {
    private String email;
    private String username;
    private String password;

    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .build();
    }
}

package com.korit.security_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInReqDto {
    private String email;
    private String password;
}

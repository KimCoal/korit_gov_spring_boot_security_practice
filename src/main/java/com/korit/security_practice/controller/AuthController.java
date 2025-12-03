package com.korit.security_practice.controller;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.SignInReqDto;
import com.korit.security_practice.dto.SignUpReqDto;
import com.korit.security_practice.security.model.Principal;
import com.korit.security_practice.service.AuthService;
import com.korit.security_practice.service.MailService;
import com.korit.security_practice.service.VerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final VerifyService verifyService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpReqDto signupReqDto) {
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInReqDto signInReqDto) {
        return ResponseEntity.ok(authService.signin(signInReqDto));
    }

    @GetMapping("/verify/code")
    public ResponseEntity<?> requestVerifyCode(@AuthenticationPrincipal Principal principal) {
        Integer userId = principal.getUserId();
        System.out.println("user id : " + userId);
        return ResponseEntity.ok(verifyService.getVerifyCode(userId));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String code, @AuthenticationPrincipal Principal principal) {
        Integer userId = principal.getUserId();
        return ResponseEntity.ok(verifyService.verifyUser(userId, code));
    }



}

package com.korit.security_practice.controller;

import com.korit.security_practice.dto.ModifyPasswordReqDto;
import com.korit.security_practice.dto.ModifyUsernameReqDto;
import com.korit.security_practice.security.model.Principal;
import com.korit.security_practice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/mod/password")
    public ResponseEntity<?> modifyPassword(@RequestBody ModifyPasswordReqDto modifyPasswordReqDto,
                                            @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(accountService.modifyPassword(modifyPasswordReqDto, principal));
    }

    @PostMapping("/mod/username")
    public ResponseEntity<?> modifyUsername(@RequestBody ModifyUsernameReqDto modifyUsernameReqDto,
                                            @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(accountService.modifyUsername(modifyUsernameReqDto, principal));
    }

}

package com.korit.security_practice.controller;

import com.korit.security_practice.dto.ApiRespDto;
import com.korit.security_practice.dto.SendMailReqDto;
import com.korit.security_practice.security.model.Principal;
import com.korit.security_practice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestBody SendMailReqDto sendMailReqDto,
                                      @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(mailService.sendMail(sendMailReqDto, principal));
    }

    @GetMapping("/verify-page")
    public String verifyPage() {
        return "verify_page";
    }

    @PostMapping("/verify")
    public String mailVerify(@RequestParam String email, @RequestParam String code, Model model) {
        ApiRespDto<?> result = mailService.verifyUser(email, code);
        model.addAttribute("message", result.getMessage());
        return "result_page";
    }
}

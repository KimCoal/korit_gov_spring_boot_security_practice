package com.korit.security_practice.security.handler;

import com.korit.security_practice.entity.OAuth2User;
import com.korit.security_practice.entity.User;
import com.korit.security_practice.repository.OAuth2UserRepository;
import com.korit.security_practice.repository.UserRepository;
import com.korit.security_practice.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2UserRepository oAuth2UserRepository;

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String provider = defaultOAuth2User.getAttribute("provider");
        String providerUserId = defaultOAuth2User.getAttribute("providerUserId");
        String email = defaultOAuth2User.getAttribute("email");
        System.out.println(provider);
        System.out.println(providerUserId);
        System.out.println(email);

        Optional<OAuth2User> foundOAuth2User = oAuth2UserRepository.findOAuth2UserByProviderAndProviderUserId(provider, providerUserId);
        if (foundOAuth2User.isEmpty()) {
            response.sendRedirect("http://localhost:3000/auth/oauth2?provider=" + provider + "&providerUserId" + providerUserId + "&email=" + email);
            return;
        }

        Optional<User> foundUser = userRepository.findUserByUserId(foundOAuth2User.get().getUserId());
        String accessToken = null;
        if (foundUser.isPresent()) {
            accessToken = jwtUtils.generateAccessToken(Integer.toString(foundUser.get().getUserId()));
        }

        response.sendRedirect("http://localhost:3000/auth/oauth2/signin?accessToken=" + accessToken);
    }
}
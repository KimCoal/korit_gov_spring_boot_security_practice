package com.korit.security_practice.repository;

import com.korit.security_practice.entity.OAuth2User;
import com.korit.security_practice.mapper.OAuth2UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuth2UserRepository {
    private final OAuth2UserMapper oAuth2UserMapper;

    public Optional<OAuth2User> findOAuth2UserByProviderAndProviderUserId(String provider, String providerUserId) {
        return oAuth2UserMapper.findOAuth2UserByProviderAndProviderUserId(provider, providerUserId);
    }

    public int addOAuth2User (OAuth2User oAuth2User) {
        return oAuth2UserMapper.addOAuth2User(oAuth2User);
    }
}

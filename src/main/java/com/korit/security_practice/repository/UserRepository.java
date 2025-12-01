package com.korit.security_practice.repository;

import com.korit.security_practice.entity.User;
import com.korit.security_practice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public Optional<User> findUserByUserId (Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    public Optional<User> findUserByEmail (String email) {
        return userMapper.findUserByUserEmail(email);
    }

    public Optional<User> findUserByUsername (String username) {
        return userMapper.findUserByUsername(username);
    }

    public Optional<User> addUser (User user) {
        try {
            userMapper.addUser(user);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public int modifyPassword(User user) {
        return userMapper.modifyPassword(user);
    }

    public int modifyUsername(User user) {
        return userMapper.modifyUsername(user);
    }
}

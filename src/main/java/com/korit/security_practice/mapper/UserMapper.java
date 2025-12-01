package com.korit.security_practice.mapper;

import com.korit.security_practice.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findUserByUserId (Integer userId);
    Optional<User> findUserByUserEmail (String email);
    Optional<User> findUserByUsername (String username);
    int addUser(User user);
    int modifyPassword(User user);
    int modifyUsername(User user);
}

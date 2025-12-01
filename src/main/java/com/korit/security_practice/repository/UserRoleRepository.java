package com.korit.security_practice.repository;

import com.korit.security_practice.entity.UserRole;
import com.korit.security_practice.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRoleRepository {
    private final UserRoleMapper userRoleMapper;

    public void addUserRole(UserRole userRole) {
        userRoleMapper.addUserRole(userRole);
    }

    public int modifyRole(UserRole userRole) {
       return userRoleMapper.modifyRole(userRole);
    }
}

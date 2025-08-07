package com.java.loginReg.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.java.loginReg.dataAccess.UserRepository;
import com.java.loginReg.entities.User;

public class SecurityUtil {
    public static User getCurrentUser(UserRepository userRepo) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
    }
}

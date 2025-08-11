package com.java.healSync.dto;

import com.java.healSync.enums.Role;

public class AuthResponseDto {
    private String token;
    private Role role;
    private Long userId;
    private String firstName;
    private String lastName;

    public AuthResponseDto(String token, Role role, Long userId, String firstName, String lastName) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AuthResponseDto{" +
                "token='" + token + '\'' +
                ", role=" + role +
                ", userId=" + userId +
                '}';
    }
}


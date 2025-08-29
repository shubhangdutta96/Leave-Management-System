package com.example.project.config;

import lombok.Getter;

@Getter
public class JwtAuthenticatedUser {
    private Long employeeId;
    private String username;
    private String role;

    public JwtAuthenticatedUser(Long employeeId, String username, String role) {
        this.employeeId = employeeId;
        this.username = username;
        this.role = role;
    }

}


package com.thomrdev.aiproject.ai_project.domain.model;

import lombok.Builder;

@Builder
public class User {
    private Long id;
    private String email;
    private String name;
    private String password;
    private String role;
}
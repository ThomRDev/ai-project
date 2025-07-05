package com.thomrdev.aiproject.ai_project.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String password;

    private String role;
}
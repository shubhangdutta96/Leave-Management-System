package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // can be email

    @Column(nullable = false)
    private String password; // store hashed password only

    @Enumerated(EnumType.STRING)
    private Role role;   // TODO: RBAC (Role back access control)

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


}

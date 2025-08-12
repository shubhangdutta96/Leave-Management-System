package com.example.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String department;
    private LocalDate joiningDate;
    private static final int DEFAULT_LEAVE_BALANCE = 30;
    @Builder.Default
    private Integer leaveBalance = DEFAULT_LEAVE_BALANCE;
    @Version
    private Integer version;
}
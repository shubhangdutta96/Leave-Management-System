package com.example.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateEmployeeRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    private String department;
    @NotNull
    private LocalDate joiningDate;
    private Integer leaveBalance;
}

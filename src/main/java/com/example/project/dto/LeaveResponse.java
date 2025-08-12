package com.example.project.dto;

import com.example.project.entity.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class LeaveResponse {
    private Long id;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private LeaveStatus status;
    private String reason;
}

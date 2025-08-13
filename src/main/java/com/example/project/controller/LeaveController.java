package com.example.project.controller;

import com.example.project.dto.ApplyLeaveRequest;
import com.example.project.dto.LeaveResponse;
import com.example.project.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveController {
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponse> applyLeave(@Valid @RequestBody ApplyLeaveRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leaveService.applyLeave(req));

    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.approveLeave(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.rejectLeave(id));
    }

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> listAllLeaves() {
        return ResponseEntity.ok(leaveService.listAllLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponse> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponse>> getLeavesForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveService.getLeavesForEmployee(employeeId));
    }

    @GetMapping("healthCheck")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Service is running");
        return ResponseEntity.ok("Leave Service is running");
    }
}

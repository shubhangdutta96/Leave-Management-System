package com.example.project.controller;

import com.example.project.config.JwtAuthenticatedUser;
import com.example.project.dto.ApplyLeaveRequest;
import com.example.project.dto.LeaveResponse;
import com.example.project.entity.Employee;
import com.example.project.service.EmployeeService;
import com.example.project.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/*
Features for employee persona :-
-apply leave
-check leave balance
-get employee by id
-get leave by id
-update leave
-delete leave
 */

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponse> applyLeave(@Valid @RequestBody ApplyLeaveRequest req) {
        Long currentUserId = leaveService.getCurrentEmployeeId();

        if (!req.getEmployeeId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        req.setEmployeeId(currentUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leaveService.applyLeave(req));
    }

    @GetMapping("/{id}/leave-balance")
    public ResponseEntity<Integer> getLeaveBalance(@PathVariable Long id) {
        Long currentUserId = leaveService.getCurrentEmployeeId();
        if (!id.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(employeeService.findById(id).getLeaveBalance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Long currentUserId = leaveService.getCurrentEmployeeId();
        if (!id.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping("/leave/{id}")
    public ResponseEntity<LeaveResponse> getLeaveById(@PathVariable Long id) {
        Long currentUserId = leaveService.getCurrentEmployeeId();
        LeaveResponse leave = leaveService.getLeaveById(id);

        // Only allow access if this leave belongs to current employee
        if (!leave.getEmployeeId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(leave);
    }
    @PutMapping("/leave/{id}")
    public ResponseEntity<LeaveResponse> updateLeave(@PathVariable Long id, @Valid @RequestBody ApplyLeaveRequest req) {
        Long currentUserId = leaveService.getCurrentEmployeeId();

        // Ensure the leave belongs to the current user
        LeaveResponse existingLeave = leaveService.getLeaveById(id);
        if (!existingLeave.getEmployeeId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Ensure update request is for current employee only
        req.setEmployeeId(currentUserId);

        LeaveResponse updatedLeave = leaveService.updateLeave(id, req); // Implement this in your service
        return ResponseEntity.ok(updatedLeave);
    }
    @DeleteMapping("/leave/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {
        Long currentUserId = leaveService.getCurrentEmployeeId();

        LeaveResponse leave = leaveService.getLeaveById(id);
        if (!leave.getEmployeeId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        leaveService.deleteLeave(id); // Implement this in your service
        return ResponseEntity.noContent().build();
    }


    @GetMapping("healthCheck")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Service is running");
        return ResponseEntity.ok("Leave Service is running");
    }
}

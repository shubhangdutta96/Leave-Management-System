package com.example.project.controller;


import com.example.project.dto.LeaveResponse;
import com.example.project.entity.Employee;
import com.example.project.service.EmployeeService;
import com.example.project.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Features of Manager Dashboard:-
-get employees
-get employee by id
-get all leaves
-get leave by id
-approve
-reject
 */

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    LeaveService leaveService;
    @GetMapping("/get-employees")
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok(employeeService.listAll());
    }
    @GetMapping("/get-employee/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }
    @GetMapping("/leaves")
    public ResponseEntity<List<LeaveResponse>> listAllLeaves() {
        return ResponseEntity.ok(leaveService.listAllLeaves());
    }
    @GetMapping("/leave/{id}")
    public ResponseEntity<LeaveResponse> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }
    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.approveLeave(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.rejectLeave(id));
    }
    @GetMapping("healthCheck")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Service is running");
        return ResponseEntity.ok("Leave Service is running");
    }
}

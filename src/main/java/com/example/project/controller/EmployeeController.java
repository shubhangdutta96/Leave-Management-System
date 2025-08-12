package com.example.project.controller;

import com.example.project.dto.CreateEmployeeRequest;
import com.example.project.entity.Employee;
import com.example.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(req));

    }

    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok(employeeService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping("/{id}/leave-balance")
    public ResponseEntity<Integer> getLeaveBalance(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id).getLeaveBalance());
    }
}

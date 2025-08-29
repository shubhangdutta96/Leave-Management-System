package com.example.project.controller;

import com.example.project.dto.CreateEmployeeRequest;
import com.example.project.entity.Employee;
import com.example.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Features for admin dashboard:-
-create employee
-get employees
-get employee by id
-update employee
*/

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    EmployeeService employeeService;
    @PostMapping("/create-employee")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody CreateEmployeeRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(req));

    }
    @GetMapping("/get-employees")
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok(employeeService.listAll());
    }
    @GetMapping("/get-employee/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }
    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck() {
        System.out.println("Service is running");
        return ResponseEntity.ok("Leave Service is running");
    }
}

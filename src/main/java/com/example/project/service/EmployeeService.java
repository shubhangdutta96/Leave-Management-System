package com.example.project.service;

import com.example.project.dto.CreateEmployeeRequest;
import com.example.project.entity.Employee;
import com.example.project.exception.ApiException;
import com.example.project.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(CreateEmployeeRequest req) {
        if (employeeRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new ApiException("Employee with this email already exists");
        }
        Employee employee = Employee.builder()
                .name(req.getName())
                .email(req.getEmail())
                .department(req.getDepartment())
                .joiningDate(req.getJoiningDate())
                .leaveBalance(Optional.ofNullable(req.getLeaveBalance()).orElse(30))
                .build();
        logger.info("Creating new employee: {}", employee.toString());
        if (employee.getLeaveBalance() < 0) {
            throw new ApiException("Leave balance cannot be negative");
        }
        return employeeRepository.save(employee);
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ApiException("Employee not found"));
    }

    public List<Employee> listAll() {
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {
        logger.info("Saving employee: {}", employee.toString());
        return employeeRepository.save(employee);
    }
}
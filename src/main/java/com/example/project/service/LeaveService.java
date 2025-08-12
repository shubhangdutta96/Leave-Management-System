package com.example.project.service;

import com.example.project.dto.ApplyLeaveRequest;
import com.example.project.dto.LeaveResponse;
import com.example.project.entity.Employee;
import com.example.project.entity.LeaveRequest;
import com.example.project.entity.LeaveStatus;
import com.example.project.exception.ApiException;
import com.example.project.repository.LeaveRequestRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeaveService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LeaveService.class);
    private final LeaveRequestRepository leaveRepository;
    private final EmployeeService employeeService;

    public LeaveService(LeaveRequestRepository leaveRepository, EmployeeService employeeService) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
    }

    public LeaveResponse applyLeave(@Valid ApplyLeaveRequest req) {
        Employee emp = employeeService.findById(req.getEmployeeId());

        if (req.getEndDate().isBefore(req.getStartDate())) {
            logger.info("Invalid leave request: end date {} is before start date {}", req.getEndDate(), req.getStartDate());
            throw new ApiException("End date cannot be before start date");
        }
        if (req.getStartDate().isBefore(emp.getJoiningDate())) {
            logger.info("Leave request before joining date: start date {} is before employee joining date {}", req.getStartDate(), emp.getJoiningDate());
            throw new ApiException("Leave cannot be applied before joining date");
        }

        long days = ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1;
        if (days > emp.getLeaveBalance()) {
            logger.info("Insufficient leave balance: requested {} days, available {}", days, emp.getLeaveBalance());
            throw new ApiException("Insufficient leave balance");
        }

        if (!leaveRepository.findAnyOverlapping(emp, req.getStartDate(), req.getEndDate()).isEmpty()) {
            logger.info("Overlapping leave request exists for employee {}: requested {} to {}", emp.getId(), req.getStartDate(), req.getEndDate());
            throw new ApiException("Overlapping leave request exists");
        }

        LeaveRequest lr = LeaveRequest.builder()
                .employee(emp)
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .status(LeaveStatus.PENDING)
                .days((int) days)
                .reason(req.getReason())
                .build();

        LeaveRequest saved = leaveRepository.save(lr);
        logger.info("Leave request created: {}", saved.toString());
        return new LeaveResponse(saved.getId(), emp.getId(), saved.getStartDate(), saved.getEndDate(), saved.getDays(), saved.getStatus(), saved.getReason());
    }

    public LeaveResponse approveLeave(Long id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow(() -> new ApiException("Leave not found"));
        if (lr.getStatus() != LeaveStatus.PENDING) {
            throw new ApiException("Leave already processed");
        }
        Employee emp = lr.getEmployee();
        if (lr.getDays() > emp.getLeaveBalance()) {
            throw new ApiException("Insufficient balance to approve");
        }
        emp.setLeaveBalance(emp.getLeaveBalance() - lr.getDays());
        lr.setStatus(LeaveStatus.APPROVED);
        employeeService.save(emp);
        leaveRepository.save(lr);
        LeaveResponse leaveResponse = new LeaveResponse(lr.getId(), emp.getId(), lr.getStartDate(), lr.getEndDate(), lr.getDays(), lr.getStatus(), lr.getReason());
        logger.info("Leave request approved: {}", leaveResponse.toString());
        return leaveResponse;
    }

    public LeaveResponse rejectLeave(Long id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow(() -> new ApiException("Leave not found"));
        if (lr.getStatus() != LeaveStatus.PENDING) {
            throw new ApiException("Leave already processed");
        }
        lr.setStatus(LeaveStatus.REJECTED);
        leaveRepository.save(lr);
        Employee emp = lr.getEmployee();
        LeaveResponse leaveResponse = new LeaveResponse(lr.getId(), emp.getId(), lr.getStartDate(), lr.getEndDate(), lr.getDays(), lr.getStatus(), lr.getReason());
        logger.info("Leave request rejected: {}", leaveResponse.toString());
        return leaveResponse;
    }

    public LeaveResponse getLeaveById(Long id) {
        LeaveRequest lr = leaveRepository.findById(id).orElseThrow(() -> new ApiException("Leave not found"));
        LeaveResponse leaveResponse = new LeaveResponse(lr.getId(), lr.getEmployee().getId(), lr.getStartDate(), lr.getEndDate(), lr.getDays(), lr.getStatus(), lr.getReason());
        logger.info("Fetched leave request: {}", leaveResponse.toString());
        return leaveResponse;
    }

    public List<LeaveResponse> getLeavesForEmployee(Long empId) {
        Employee emp = employeeService.findById(empId);
        return leaveRepository.findByEmployee(emp).stream()
                .map(lr -> new LeaveResponse(lr.getId(), emp.getId(), lr.getStartDate(), lr.getEndDate(), lr.getDays(), lr.getStatus(), lr.getReason()))
                .collect(Collectors.toList());
    }
}

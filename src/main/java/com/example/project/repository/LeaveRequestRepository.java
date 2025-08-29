package com.example.project.repository;

import com.example.project.entity.Employee;
import com.example.project.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(Employee employee);
    @Query("select lr from LeaveRequest lr where lr.employee = :employee and lr.status = 'APPROVED' and not (lr.endDate < :startDate or lr.startDate > :endDate)")
    List<LeaveRequest> findApprovedOverlapping(@Param("employee") Employee employee,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    @Query("select lr from LeaveRequest lr where lr.employee = :employee and not (lr.endDate < :startDate or lr.startDate > :endDate)")
    List<LeaveRequest> findAnyOverlapping(@Param("employee") Employee employee,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee = :emp AND lr.id <> :excludeId AND " +
            "((lr.startDate <= :endDate AND lr.endDate >= :startDate))")
    List<LeaveRequest> findAnyOverlappingExcludingLeave(@Param("emp") Employee emp,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("excludeId") Long excludeId);
}

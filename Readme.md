# Employee Leave Management System

A comprehensive Spring Boot-based REST API for managing employee leave requests with automated balance tracking, approval workflows, and audit trails.

---

## 📚 Table of Contents
- [Setup & Installation](#setup--installation)
- [System Architecture](#system-architecture)
- [API Documentation](#api-documentation)
- [High-Level Design](#high-level-design)
- [Edge Cases Handled](#edge-cases-handled)
- [Assumptions](#assumptions)
- [Potential Improvements](#potential-improvements)
- [Scaling Strategy](#scaling-strategy)
- [Support & Deployment](#support--deployment)

---

## setup--installation

### **Prerequisites**
- Java 17+
- Maven 3.6+
- PostgreSQL 12+ (H2 for development)
- Git
- Postman (optional)

### **Local Development Setup**
``` bash
git clone https://github.com/your-org/employee-leave-system.git
cd employee-leave-system
```

### **Architecture diagram**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│                 │    │                 │    │                 │
│    Frontend     │◄──►│    Backend      │◄──►│    Database     │
│                 │    │                 │    │                 │
│  • Web Client   │    │  • Spring Boot  │    │  • PostgreSQL   │
│  • Mobile App   │    │  • REST APIs    │    │  • JPA/Hibernate│
│  • Admin Panel  │    │  • Validation   │    │  • ACID Trans   │
│                 │    │  • Logging      │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### **high-level-design**
```
┌─────────────────────────────────────┐
│              Employee               │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - name: String                      │
│ - email: String (UNIQUE)            │
│ - department: String                │
│ - joiningDate: LocalDate            │
│ - leaveBalance: Integer             │
├─────────────────────────────────────┤
│ + getId(): Long                     │
│ + setLeaveBalance(Integer): void    │
│ + getEmail(): String                │
└─────────────────────────────────────┘
                    │ 1
                    │
                    │ has
                    │
                    │ *
┌─────────────────────────────────────┐
│            LeaveRequest             │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - employee: Employee                │
│ - startDate: LocalDate              │
│ - endDate: LocalDate                │
│ - days: Integer                     │
│ - status: LeaveStatus               │
│ - reason: String                    │
├─────────────────────────────────────┤
│ + getDays(): Integer                │
│ + getStatus(): LeaveStatus          │
│ + setStatus(LeaveStatus): void      │
└─────────────────────────────────────┘
                    │
                    │ uses
                    │
┌─────────────────────────────────────┐
│            LeaveStatus              │
│            <<enumeration>>          │
├─────────────────────────────────────┤
│ + PENDING                           │
│ + APPROVED                          │
│ + REJECTED                          │
└─────────────────────────────────────┘
```

### **system-architecture**
```
┌─────────────────────────────────────┐
│          EmployeeService            │
├─────────────────────────────────────┤
│ - employeeRepository                │
├─────────────────────────────────────┤
│ + createEmployee()                  │
│ + findById()                        │
│ + listAll()                         │
│ + save()                            │
└─────────────────────────────────────┘
                │
                │ depends on
                ▼
┌─────────────────────────────────────┐
│           LeaveService              │
├─────────────────────────────────────┤
│ - leaveRepository                   │
│ - employeeService                   │
├─────────────────────────────────────┤
│ + applyLeave()                      │
│ + approveLeave()                    │
│ + rejectLeave()                     │
│ + getLeaveById()                    │
│ + getLeavesForEmployee()            │
└─────────────────────────────────────┘
```

### **Database Schema**
```
Employee Table:

CREATE TABLE IF NOT EXISTS employees (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
email VARCHAR(150) UNIQUE NOT NULL,
department VARCHAR(100),
joining_date DATE NOT NULL,
leave_balance INT DEFAULT 30,
version INT DEFAULT 0
);

leave_requests Table:

CREATE TABLE IF NOT EXISTS leave_requests (
id SERIAL PRIMARY KEY,
employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
start_date DATE NOT NULL,
end_date DATE NOT NULL,
status VARCHAR(20) DEFAULT 'PENDING',
days INT NOT NULL,
reason TEXT
);
```

## api-documentation

### **Endpoints**

| HTTP Method | Endpoint                   | Description                 |
|-------------|----------------------------|-----------------------------|
| **POST**    | `/employees`               | Create new employee         | 
| **GET**     | `/employees`               | List all employees          | 
| **GET**     | `/employees/{id}`          | Get employee by ID          |
| **PUT**     | `/employees/{id}`          | Update employee             |
| **POST**    | `/leaves`                  | Apply for leave             |
| **GET**     | `/leaves`                  | Get all leaves              | 
| **GET**     | `/leaves/{id}`             | Get leave by ID             | 
| **PUT**     | `/leaves/{id}/approve`     | Approve leave               | 
| **PUT**     | `/leaves/{id}/reject`      | Reject leave                |
| **GET**     | `/employees/{id}/leaves`   | Get employee's leaves       | 
| **GET**     | `/analytics/leaves`        | Leave analytics             | 

---

### **Example: Create Employee**
```http
POST /employees
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "department": "Engineering",
  "joiningDate": "2024-01-15",
  "leaveBalance": 30
}
```

## edge-cases-handled
- Prevents re-processing approved/rejected leaves
- Leave start date before joining date → blocked
- End date before start date → blocked
- Leave request in the past (optional rule)
- Insufficient leave balance → blocked
- Overlapping leave request exists → blocked
- Duplicate employee email → blocked
- Updating/deleting employee with active leaves → prevented
- Approving/rejecting already processed leave → blocked
- Negative leave balance → prevented
- Concurrency issues when multiple managers approve simultaneously → handled with transaction locks
- Weekend/holiday exclusion in leave calculation (future enhancement)

---

## assumptions
**Business**
- Default 30 leave days per year
- Full-day leaves only
- Single-level approval process
- All days counted as working days

**Technical**
- PostgreSQL in production, H2 for testing
- Transaction management via `@Transactional`

---

## potential-improvements
- Add authentication & role-based access control
- Email/SMS notifications
- Pagination for large datasets
- Multiple leave types & business calendar support

---

## scaling-strategy
- **Current**: Single instance, 50–100 concurrent users
- **Next**: Vertical scaling → Horizontal scaling → Microservices
- **Enhancements**: API Gateway, load balancing

---

## Support & Deployment
- Deployable on AWS EC2 or Heroku
- Health check: `/api/actuator/health`


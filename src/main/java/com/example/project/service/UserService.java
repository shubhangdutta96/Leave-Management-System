package com.example.project.service;
import com.example.project.entity.Credentials;
import com.example.project.entity.Employee;
import com.example.project.entity.Users;
import com.example.project.repository.EmployeeRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmployeeRepository employeeRepository;
    private JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, JwtService jwtService) {
        this.userRepository = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
    }


    @Transactional
    public ResponseEntity<String> register(Users user) {
        System.out.println("from user service...");
//         Optionally check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        } else {
            // New User, Encode the password before saving
            System.out.println("encoding password...");
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            System.out.println("Password successfully encoded!" + encodedPassword);
        }

        System.out.println("user successfully registered!");
        // Ensure the employee is managed (not detached)
        if (user.getEmployee() != null) {
            Employee employee = employeeRepository.findById(user.getEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            user.setEmployee(employee);  // Attach the managed employee to the user
        }
        userRepository.saveAndFlush(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered!");
    }

    public ResponseEntity<String> verify(Credentials credentials) {
        System.out.println("from user service...");
        try {
            Users existingUser = userRepository.findByUsername(credentials.getUsername())
                    .orElseThrow(() -> new RuntimeException("Not in Database, kindly register first!"));

            if (!passwordEncoder.matches(credentials.getPassword(), existingUser.getPassword())) {
                throw new RuntimeException("Password incorrect");
            }

            String token = jwtService.generateToken(existingUser.getUsername(), existingUser.getRole(), existingUser.getEmployee().getId());
            return ResponseEntity.ok("Login successful, Authentication token: " + token);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }
}
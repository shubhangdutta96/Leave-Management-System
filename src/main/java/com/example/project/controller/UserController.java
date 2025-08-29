package com.example.project.controller;
import com.example.project.entity.Credentials;
import com.example.project.entity.Users;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        System.out.println("registering user...");
        return service.register(user);
    }

    @PutMapping("/login")
    public ResponseEntity<String> login(@RequestBody Credentials credentials){
        System.out.println("verifying user...");
        return service.verify(credentials);
    }

}

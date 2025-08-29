package com.example.project.entity;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
        private String username; // can be email
        private String password; // store hashed password only
}

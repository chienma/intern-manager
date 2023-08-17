package com.example.internmanager.auth;

import com.example.internmanager.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private Role role;
}

package com.example.student_course_management_system.request.auth;

import com.example.student_course_management_system.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


public class SignUpRequest {
    @NotBlank
    @Size(min = 3,max = 15)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8,max = 32)
    private String password;

    private UserRole[] roles;

    public SignUpRequest() {
    }

    public SignUpRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public SignUpRequest(String username, String email, String password, UserRole[] roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public @NotBlank @Size(min = 3, max = 15) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 3, max = 15) String username) {
        this.username = username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 8, max = 32) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 32) String password) {
        this.password = password;
    }

    public UserRole[] getRoles() {
        return roles;
    }

    public void setRoles(UserRole[] roles) {
        this.roles = roles;
    }
}

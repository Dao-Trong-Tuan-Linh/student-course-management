package com.example.student_course_management_system.request.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank
    @Min(3)
    private String usernameOrEmail;

    @NotBlank
    @Size(min = 8,max = 32)
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public @NotBlank @Min(3) String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(@NotBlank @Min(3) String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public @NotBlank @Size(min = 8, max = 32) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 32) String password) {
        this.password = password;
    }
}

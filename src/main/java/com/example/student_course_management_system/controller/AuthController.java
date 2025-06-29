package com.example.student_course_management_system.controller;

import com.example.student_course_management_system.request.auth.LoginRequest;
import com.example.student_course_management_system.request.auth.SignUpRequest;
import com.example.student_course_management_system.response.JwtAuthenticationResponse.JwtAuthenticationResponse;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signupController(@Valid @RequestBody SignUpRequest signupRequest) {
        return authService.signupService(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse> loginController(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.loginService(loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshController(HttpServletRequest request) {
        return authService.refreshService(request);
    }
}

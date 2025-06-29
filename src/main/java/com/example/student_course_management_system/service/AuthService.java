package com.example.student_course_management_system.service;

import com.example.student_course_management_system.request.auth.LoginRequest;
import com.example.student_course_management_system.request.auth.SignUpRequest;
import com.example.student_course_management_system.response.JwtAuthenticationResponse.JwtAuthenticationResponse;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ApiResponse> signupService(SignUpRequest signupRequest);
    ResponseEntity<ApiDataResponse> loginService(LoginRequest loginRequest);
    ResponseEntity<JwtAuthenticationResponse> refreshService(HttpServletRequest request);
}

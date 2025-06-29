package com.example.student_course_management_system.response.JwtAuthenticationResponse;

public class JwtAuthenticationResponse {
    private final String accessToken;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

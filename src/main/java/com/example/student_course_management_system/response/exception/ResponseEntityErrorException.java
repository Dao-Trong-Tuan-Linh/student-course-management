package com.example.student_course_management_system.response.exception;

import com.example.student_course_management_system.response.common.ApiResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseEntityErrorException extends RuntimeException{
    private final transient ResponseEntity<ApiResponse> apiResponseResponseEntity;

    public ResponseEntityErrorException(ResponseEntity<ApiResponse> apiResponseResponseEntity) {
        this.apiResponseResponseEntity = apiResponseResponseEntity;
    }
}

package com.example.student_course_management_system.response.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;


    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

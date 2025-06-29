package com.example.student_course_management_system.response.exception;

import com.example.student_course_management_system.response.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
    private final ApiResponse apiResponse;

    public InternalServerException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }


    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}

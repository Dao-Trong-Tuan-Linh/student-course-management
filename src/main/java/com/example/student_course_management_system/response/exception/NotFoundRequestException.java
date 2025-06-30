package com.example.student_course_management_system.response.exception;

import com.example.student_course_management_system.response.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundRequestException extends RuntimeException {
    private ApiResponse apiResponse;

    public NotFoundRequestException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}

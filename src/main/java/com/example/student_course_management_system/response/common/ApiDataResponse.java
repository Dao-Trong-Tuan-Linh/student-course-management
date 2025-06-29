package com.example.student_course_management_system.response.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiDataResponse<T> extends ApiResponse {
    private T data;

    public ApiDataResponse(ApiResponse apiResponse, T data) {
        super(apiResponse.getSuccess(), apiResponse.getMessage());
        this.data = data;
    }
}

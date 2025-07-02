package com.example.student_course_management_system.utils;

import com.example.student_course_management_system.response.exception.BadRequestException;

public class AppUtils {
    public static void validatePageAndSize(int page,int size) {
        if(page <= 0) {
            throw new BadRequestException("page phải lớn hơn 0");
        }

        if(size <= 0) {
            throw new BadRequestException("size phải lớn hơn 0");
        }
    }
}

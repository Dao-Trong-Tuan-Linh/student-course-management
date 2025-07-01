package com.example.student_course_management_system.service;

import com.example.student_course_management_system.request.course.CourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseEntity<ApiDataResponse> createService(CourseRequest courseRequest);
}

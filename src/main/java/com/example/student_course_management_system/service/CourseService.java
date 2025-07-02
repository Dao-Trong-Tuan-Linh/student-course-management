package com.example.student_course_management_system.service;

import com.example.student_course_management_system.dto.course.CourseDto;
import com.example.student_course_management_system.model.Course;
import com.example.student_course_management_system.request.course.CourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    ResponseEntity<ApiDataResponse> createService(CourseRequest courseRequest);
    PagedResponse<CourseDto> getListService(int page, int size, String search);
}

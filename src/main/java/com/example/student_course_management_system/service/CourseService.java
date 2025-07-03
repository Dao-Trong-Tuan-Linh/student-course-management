package com.example.student_course_management_system.service;

import com.example.student_course_management_system.dto.course.CourseDto;
import com.example.student_course_management_system.model.Course;
import com.example.student_course_management_system.request.course.CourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.response.common.PagedResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CourseService {
    ResponseEntity<ApiDataResponse> createService(CourseRequest courseRequest);
    PagedResponse<CourseDto> getListService(int page, int size, String search, List<Long> genreIds);
    ResponseEntity<ApiDataResponse> updateService(Long id, CourseRequest courseRequest);
    ResponseEntity<ApiResponse> deleteService(Long id);
}

package com.example.student_course_management_system.service;

import com.example.student_course_management_system.model.StudentCourse;
import com.example.student_course_management_system.request.student_course.StudentCourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.PagedResponse;
import com.example.student_course_management_system.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentCourseService {
    ResponseEntity<ApiDataResponse> createService(StudentCourseRequest studentCourseRequest, UserPrincipal userPrincipal);
    PagedResponse<StudentCourse> getStudentCourseService(
            UserPrincipal userPrincipal,
            int page,
            int size,
            String search,
            List<Long> genreIds
    );
    ResponseEntity<ApiDataResponse> cancelCourseService(UserPrincipal userPrincipal, StudentCourseRequest studentCourseRequest);
}

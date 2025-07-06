package com.example.student_course_management_system.controller;

import com.example.student_course_management_system.model.StudentCourse;
import com.example.student_course_management_system.request.student_course.StudentCourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.response.common.PagedResponse;
import com.example.student_course_management_system.security.CurrentUser;
import com.example.student_course_management_system.security.UserPrincipal;
import com.example.student_course_management_system.service.StudentCourseService;
import com.example.student_course_management_system.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-course")
public class StudentCourseController {
    @Autowired
    private StudentCourseService studentCourseService;

    @PostMapping
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiDataResponse> registerCourseController(
            @Valid @RequestBody StudentCourseRequest studentCourseRequest,
            @CurrentUser UserPrincipal currentStudent
    ) {
        System.out.println("currentStudent: " + currentStudent);
        return this.studentCourseService.createService(studentCourseRequest, currentStudent);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('STUDENT')")
    public PagedResponse<StudentCourse> getMyCoursesController(
            @CurrentUser UserPrincipal currentStudent,
            @RequestParam(name="page",required = false,defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size",required = false,defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "search",required = false) String search,
            @RequestParam(name = "genreId",required = false) List<Long> genreIds
    ) {
        return this.studentCourseService.getStudentCourseService(currentStudent,page,size,search,genreIds);
    }

    @PutMapping("/cancel")
    @PreAuthorize("hasAuthority('STUDENT')")
    public ResponseEntity<ApiDataResponse> cancelCourseController(
            @CurrentUser UserPrincipal currentStudent,
            @Valid @RequestBody StudentCourseRequest studentCourseRequest
    ) {
        return this.studentCourseService.cancelCourseService(currentStudent, studentCourseRequest);
    }
}

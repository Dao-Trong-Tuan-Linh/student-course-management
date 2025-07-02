package com.example.student_course_management_system.controller;

import com.example.student_course_management_system.dto.course.CourseDto;
import com.example.student_course_management_system.model.Course;
import com.example.student_course_management_system.request.course.CourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.PagedResponse;
import com.example.student_course_management_system.service.CourseService;
import com.example.student_course_management_system.utils.AppConstants;
import com.example.student_course_management_system.utils.AppUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiDataResponse> createController(@Valid @RequestBody CourseRequest courseRequest) {
        return this.courseService.createService(courseRequest);
    }

    @GetMapping("/list")
    public PagedResponse<CourseDto> getListCourse(
            @RequestParam(name="page",required = false,defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size",required = false,defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "search",required = false) String search
    ) {
        AppUtils.validatePageAndSize(page,size);
        return this.courseService.getListService(page,size,search);
    }
}

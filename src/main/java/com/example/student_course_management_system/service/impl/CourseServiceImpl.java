package com.example.student_course_management_system.service.impl;

import com.example.student_course_management_system.model.Course;
import com.example.student_course_management_system.model.Genre;
import com.example.student_course_management_system.model.User;
import com.example.student_course_management_system.model.UserRole;
import com.example.student_course_management_system.repository.CourseRepository;
import com.example.student_course_management_system.repository.GenreRepository;
import com.example.student_course_management_system.repository.UserRepository;
import com.example.student_course_management_system.request.course.CourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.response.exception.BadRequestException;
import com.example.student_course_management_system.response.exception.NotFoundRequestException;
import com.example.student_course_management_system.service.CourseService;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CourseRepository courseRepository;

    private final Slugify slugify = Slugify.builder().build();

    @Override
    public ResponseEntity<ApiDataResponse> createService(CourseRequest courseRequest) {
        User existingUser = this.userRepository.findById(courseRequest.getTeacher_id()).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE,"Không tìm thấy người dùng")));
        boolean isTeacherRole =  existingUser.getRoles().stream().anyMatch(role -> role.getName() == UserRole.TEACHER);
        if(!isTeacherRole) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,"Người dùng này không phải giáo viên");
            throw new BadRequestException(apiResponse);
        }

        Genre existingGenre = this.genreRepository.findById(courseRequest.getGenre_id()).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE,"Không tìm thấy chủ đề")));

        if(Boolean.TRUE.equals(this.courseRepository.existsByName(courseRequest.getName()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,"Tên khóa học đã tồn tại");
            throw new BadRequestException(apiResponse);
        }
        String slug = slugify.slugify(courseRequest.getName());
        Course course = new Course(slug, courseRequest.getName());
        course.setTeacher(existingUser);
        course.setGenre(existingGenre);

        Course newCourse = this.courseRepository.save(course);

        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE,"Đã tạo khóa học mới thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse,newCourse);
        return new ResponseEntity<>(apiDataResponse, HttpStatus.CREATED);
    }
}

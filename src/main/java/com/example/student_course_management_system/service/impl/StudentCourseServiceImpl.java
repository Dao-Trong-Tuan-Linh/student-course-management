package com.example.student_course_management_system.service.impl;

import com.example.student_course_management_system.model.*;
import com.example.student_course_management_system.repository.CourseRepository;
import com.example.student_course_management_system.repository.GenreRepository;
import com.example.student_course_management_system.repository.StudentCourseRepository;
import com.example.student_course_management_system.repository.UserRepository;
import com.example.student_course_management_system.request.student_course.StudentCourseRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.response.common.PagedResponse;
import com.example.student_course_management_system.response.exception.BadRequestException;
import com.example.student_course_management_system.response.exception.NotFoundRequestException;
import com.example.student_course_management_system.security.UserPrincipal;
import com.example.student_course_management_system.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {
    private static String CREATED_AT = "createdAt";
    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public ResponseEntity<ApiDataResponse> createService(StudentCourseRequest studentCourseRequest, UserPrincipal userPrincipal) {
        User exisingUser = this.userRepository.findById(userPrincipal.id()).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE, "Không tìm thấy người dùng")));
        boolean isStudentRole =  exisingUser.getRoles().stream().anyMatch(role -> role.getName() == UserRole.STUDENT);
        if(!isStudentRole) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,"Người dùng này không phải học viên");
            throw new BadRequestException(apiResponse);
        }
        Course exisingCourse = this.courseRepository.findById(studentCourseRequest.getCourseId()).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE, "Không tìm thấy khóa học")));
        StudentCourseId id = new StudentCourseId(userPrincipal.id(), studentCourseRequest.getCourseId());
        Optional<StudentCourse> existingStudentCourse = this.studentCourseRepository.findById(id);
        StudentCourse studentCourse;

        if(existingStudentCourse.isPresent()) {
            studentCourse = existingStudentCourse.get();
            if(studentCourse.getStatus() == StudentCourseEnum.CANCELED) {
                studentCourse.setStatus(StudentCourseEnum.REGISTERED);
            } else {
                ApiResponse apiResponse = new ApiResponse(Boolean.FALSE,"Bạn đã đăng ký khóa học này");
                throw new BadRequestException(apiResponse);
            }
        } else {
            studentCourse = new StudentCourse(exisingUser,exisingCourse,StudentCourseEnum.REGISTERED);
        }

        StudentCourse newStudentCourse = this.studentCourseRepository.save(studentCourse);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã đăng ký khóa học thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse, newStudentCourse);
         return new ResponseEntity<>(apiDataResponse, HttpStatus.CREATED);
    }

    @Override
    public PagedResponse<StudentCourse> getStudentCourseService(UserPrincipal userPrincipal, int page, int size, String search, List<Long> genreIds) {
        Long studentId = userPrincipal.id();
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0),Math.max(size,10), Sort.Direction.DESC,CREATED_AT);
        if(genreIds != null && !genreIds.isEmpty()) {
            long countExistingGenreIds = this.genreRepository.countByIdIn(genreIds);
            if(countExistingGenreIds != genreIds.size()) {
                throw new BadRequestException("Có genre_id không hợp lệ");
            }
        }
        Page<StudentCourse> studentCourses = this.studentCourseRepository.findListBySearchStudentGenres(pageable,studentId,search,genreIds);
        if(studentCourses.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(),studentCourses.getNumber() + 1,
                    studentCourses.getSize(),studentCourses.getTotalElements()
                    ,studentCourses.getTotalPages());
        }
        return new PagedResponse<>(studentCourses.getContent(),studentCourses.getNumber() + 1,
                studentCourses.getSize(),studentCourses.getTotalElements()
                ,studentCourses.getTotalPages());
    }

    @Override
    public ResponseEntity<ApiDataResponse> cancelCourseService(UserPrincipal userPrincipal, StudentCourseRequest studentCourseRequest) {
        StudentCourseId id = new StudentCourseId(userPrincipal.id(), studentCourseRequest.getCourseId());
        StudentCourse existingStudentCourse = this.studentCourseRepository.findById(id).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE, "Không tìm thấy khóa học đã đăng ký")));
        if(existingStudentCourse.getStatus() == StudentCourseEnum.REGISTERED) {
            existingStudentCourse.setStatus(StudentCourseEnum.CANCELED);
        } else {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Khóa học này đã được hủy trước đó");
            throw new BadRequestException(apiResponse);
        }
        StudentCourse updatedStudentCourse = this.studentCourseRepository.save(existingStudentCourse);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã hủy khóa học thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse, updatedStudentCourse);
        return ResponseEntity.ok(apiDataResponse);
    }
}

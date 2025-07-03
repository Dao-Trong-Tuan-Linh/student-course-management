package com.example.student_course_management_system.service.impl;

import com.example.student_course_management_system.dto.course.CourseDto;
import com.example.student_course_management_system.mapper.CourseMapper;
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
import com.example.student_course_management_system.response.common.PagedResponse;
import com.example.student_course_management_system.response.exception.BadRequestException;
import com.example.student_course_management_system.response.exception.NotFoundRequestException;
import com.example.student_course_management_system.service.CourseService;
import com.example.student_course_management_system.utils.AppConstants;
import com.github.slugify.Slugify;
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

@Service
public class CourseServiceImpl implements CourseService {
    private static String COURSE_ID = "id";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

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
        CourseDto courseDto = courseMapper.toDto(newCourse);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE,"Đã tạo khóa học mới thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse,courseDto);
        return new ResponseEntity<>(apiDataResponse, HttpStatus.CREATED);
    }

    @Override
    public PagedResponse<CourseDto> getListService(int page, int size, String search,List<Long> genreIds) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0),Math.max(size,10), Sort.Direction.DESC,COURSE_ID);
        if(genreIds != null && !genreIds.isEmpty()) {
            long countExistingGenreIds = this.genreRepository.countByIdIn(genreIds);
            if(countExistingGenreIds != genreIds.size()) {
                throw new BadRequestException("Có genre_id không hợp lệ");
            }
        }
        Page<Course> courses = this.courseRepository.findListBySearch(pageable,search,genreIds);
        if(courses.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(),courses.getNumber() + 1,
                    courses.getSize(),courses.getTotalElements()
                    ,courses.getTotalPages());
        }
        Page<CourseDto> dtoPage = courses.map(courseMapper::toDto);
        return new PagedResponse<>(dtoPage.getContent(),courses.getNumber() + 1,
                courses.getSize(),courses.getTotalElements()
                ,courses.getTotalPages());
    }

    @Override
    public ResponseEntity<ApiDataResponse> updateService(Long id, CourseRequest courseRequest) {
        Course existingCourse = this.courseRepository.findById(id).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE,"Không tìm thấy khóa học")));
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
        String name = courseRequest.getName();
        existingCourse.setName(name);
        existingCourse.setSlug(slug);
        existingCourse.setTeacher(existingUser);
        existingCourse.setGenre(existingGenre);

        Course updatedCourse = this.courseRepository.save(existingCourse);
        CourseDto courseDto = courseMapper.toDto(updatedCourse);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã cập nhật khóa học thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse,courseDto);
        return ResponseEntity.ok(apiDataResponse);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteService(Long id) {
       Course course = this.courseRepository.findById(id).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE, "không tìm thấy khóa học")));
       this.courseRepository.deleteById(id);
       ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã xóa khóa học " + course.getName());
       return ResponseEntity.ok(apiResponse);
    }
}

package com.example.student_course_management_system.mapper;

import com.example.student_course_management_system.dto.course.CourseDto;
import com.example.student_course_management_system.dto.course.GenreSummary;
import com.example.student_course_management_system.dto.course.TeacherSummary;
import com.example.student_course_management_system.model.Course;
import com.example.student_course_management_system.model.Genre;
import com.example.student_course_management_system.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "teacher", expression = "java(toTeacherSummary(course.getTeacher()))")
    @Mapping(target = "genre",   expression = "java(toGenreSummary(course.getGenre()))")
    CourseDto toDto(Course course);

    default TeacherSummary toTeacherSummary(User user) {
        if(user == null) return null;
        return new TeacherSummary(user.getId(),user.getUsername());
    }

    default GenreSummary toGenreSummary(Genre genre) {
        if(genre == null) return null;
        return new GenreSummary(genre.getId(),genre.getName());
    }
}

package com.example.student_course_management_system.dto.course;

import java.time.LocalDateTime;

public record CourseDto(
        Long id,
        String slug,
        String name,
        TeacherSummary teacher,
        GenreSummary genre,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

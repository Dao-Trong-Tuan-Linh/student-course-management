package com.example.student_course_management_system.request.student_course;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentCourseRequest {
    @NotBlank
    private Long courseId;
}

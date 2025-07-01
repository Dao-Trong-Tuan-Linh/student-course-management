package com.example.student_course_management_system.request.course;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseRequest {
    @NotBlank
    private String name;

    @NotBlank
    private Long teacher_id;

    @NotBlank
    private  Long genre_id;
}

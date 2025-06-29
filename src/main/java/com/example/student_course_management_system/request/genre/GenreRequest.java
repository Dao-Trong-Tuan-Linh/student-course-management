package com.example.student_course_management_system.request.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenreRequest {
    @NotBlank
    private String name;
}

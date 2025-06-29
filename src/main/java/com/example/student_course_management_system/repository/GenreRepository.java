package com.example.student_course_management_system.repository;

import com.example.student_course_management_system.model.Genre;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Boolean existsByName(@NotBlank String name);
}

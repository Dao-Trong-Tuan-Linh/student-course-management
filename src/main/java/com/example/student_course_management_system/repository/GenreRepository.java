package com.example.student_course_management_system.repository;

import com.example.student_course_management_system.model.Genre;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Boolean existsByName(@NotBlank String name);

    @Query("""
           SELECT g
           FROM   Genre g
           WHERE  (:name IS NULL
                   OR :name = ''
                   OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')))
           """)
    List<Genre> findListGenres(String name);

    long countByIdIn(Collection<Long> ids);
}

package com.example.student_course_management_system.repository;

import com.example.student_course_management_system.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Boolean existsByName(String name);

    @Query("""
            SELECT c FROM Course c
            WHERE (:search IS NULL OR :search = ''
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:genreIds IS NULL OR c.genre.id IN :genreIds)
            """)
    Page<Course> findListBySearch(Pageable pageable, String search, List<Long> genreIds);
}

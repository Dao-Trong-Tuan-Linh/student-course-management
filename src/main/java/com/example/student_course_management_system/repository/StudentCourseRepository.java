package com.example.student_course_management_system.repository;

import com.example.student_course_management_system.model.StudentCourse;
import com.example.student_course_management_system.model.StudentCourseId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseId> {

    @Query("""
        SELECT c FROM StudentCourse c
        WHERE c.student.id = :studentId
        AND c.status = REGISTERED
        AND (:search IS NULL OR :search = ''
        OR LOWER(c.course.name) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:genreIds IS NULL OR c.course.genre.id IN :genreIds)
        """)
    Page<StudentCourse> findListBySearchStudentGenres(Pageable pageable, Long studentId, String search, List<Long> genreIds);
}

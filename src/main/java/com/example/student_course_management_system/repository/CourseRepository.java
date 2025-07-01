package com.example.student_course_management_system.repository;

import com.example.student_course_management_system.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Boolean existsByName(String name);
}

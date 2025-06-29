package com.example.student_course_management_system.repository;

import com.example.student_course_management_system.model.Role;
import com.example.student_course_management_system.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(UserRole name);
}

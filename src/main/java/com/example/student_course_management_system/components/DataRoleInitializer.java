package com.example.student_course_management_system.components;

import com.example.student_course_management_system.model.Role;
import com.example.student_course_management_system.model.UserRole;
import com.example.student_course_management_system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataRoleInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args)  {
        Arrays.stream(UserRole.values()).forEach(userRole -> {
            roleRepository.findByName(userRole).orElseGet(() -> {
                Role role = new Role(userRole);
                return roleRepository.save(role);
            });
        });

        System.out.println("Default roles created or already exist!");
    }
}

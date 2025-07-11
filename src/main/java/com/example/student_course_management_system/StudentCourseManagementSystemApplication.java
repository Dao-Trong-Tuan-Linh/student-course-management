package com.example.student_course_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentCourseManagementSystemApplication {
	public static void main(String[] args) {
        SpringApplication.run(StudentCourseManagementSystemApplication.class, args);
	}

	@GetMapping
	public String hello() {
		return "hello world";
	}
}

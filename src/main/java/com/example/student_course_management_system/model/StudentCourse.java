package com.example.student_course_management_system.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_course")
public class StudentCourse {
    @EmbeddedId
    private StudentCourseId id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private StudentCourseEnum status;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    public StudentCourse() {
    }

    public StudentCourse(User student, Course course, StudentCourseEnum status) {
        this.student = student;
        this.course = course;
        this.status = status;
        this.id = new StudentCourseId(student.getId(),course.getId());
    }

    public StudentCourseId getId() {
        return id;
    }

    public void setId(StudentCourseId id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public StudentCourseEnum getStatus() {
        return status;
    }

    public void setStatus(StudentCourseEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

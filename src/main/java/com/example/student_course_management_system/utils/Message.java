package com.example.student_course_management_system.utils;

import lombok.Getter;

public enum Message {
    USERNAME_EXISTS("Tên người dùng này đã tồn tại"),
    EMAIL_EXISTS("Email này đã tồn tại"),
    USER_ROLE_NOT_SET("Vai trò này chưa được tạo"),
    ACCOUNT_NOT_RIGHT("Username,email hoặc mật khẩu chưa đúng"),
    UNAUTHORIZED("Bạn không có quyền truy cập"),
    GENRE_EXISTS("Chủ đề này đã tồn tại");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

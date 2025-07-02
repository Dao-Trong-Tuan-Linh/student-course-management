package com.example.student_course_management_system.response.common;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagedResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
}

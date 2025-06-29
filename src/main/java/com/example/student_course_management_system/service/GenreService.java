package com.example.student_course_management_system.service;

import com.example.student_course_management_system.request.genre.GenreRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import org.springframework.http.ResponseEntity;

public interface GenreService {
    ResponseEntity<ApiDataResponse> createService(GenreRequest genreRequest);
}

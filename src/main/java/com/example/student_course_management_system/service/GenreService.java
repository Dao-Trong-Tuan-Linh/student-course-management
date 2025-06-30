package com.example.student_course_management_system.service;

import com.example.student_course_management_system.request.genre.GenreRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface GenreService {
    ResponseEntity<ApiDataResponse> createService(GenreRequest genreRequest);
    ResponseEntity<ApiDataResponse> getListService(String name);
    ResponseEntity<ApiDataResponse> updateService(Long id,GenreRequest genreRequest);
    ResponseEntity<ApiResponse> deleteService(Long id);
}

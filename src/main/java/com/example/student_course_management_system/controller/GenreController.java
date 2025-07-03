package com.example.student_course_management_system.controller;

import com.example.student_course_management_system.request.genre.GenreRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/genre")
public class GenreController {
    @Autowired
    private GenreService genreService;
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiDataResponse> createController(@Valid @RequestBody GenreRequest genreRequest) {
        return this.genreService.createService(genreRequest);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiDataResponse> getListController(@RequestParam(value = "name", required = false) String name) {
        return this.genreService.getListService(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiDataResponse> updateController(
            @PathVariable Long id,
            @Valid @RequestBody GenreRequest genreRequest
    ) {
        return this.genreService.updateService(id,genreRequest);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteController(@PathVariable Long id) {
        return this.genreService.deleteService(id);
    }
}

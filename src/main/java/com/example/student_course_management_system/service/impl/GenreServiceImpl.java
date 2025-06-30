package com.example.student_course_management_system.service.impl;

import com.example.student_course_management_system.model.Genre;
import com.example.student_course_management_system.repository.GenreRepository;
import com.example.student_course_management_system.request.genre.GenreRequest;
import com.example.student_course_management_system.response.common.ApiDataResponse;
import com.example.student_course_management_system.response.common.ApiResponse;
import com.example.student_course_management_system.response.exception.BadRequestException;
import com.example.student_course_management_system.response.exception.NotFoundRequestException;
import com.example.student_course_management_system.service.GenreService;
import com.example.student_course_management_system.utils.Message;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;

    private final Slugify slugify = Slugify.builder().build();

    @Override
    public ResponseEntity<ApiDataResponse> createService(GenreRequest genreRequest) {
        String name = genreRequest.getName();

        if(Boolean.TRUE.equals(genreRepository.existsByName(name))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, Message.GENRE_EXISTS.getMessage());
            throw new BadRequestException(apiResponse);
        }

        String slug = slugify.slugify(name);
        Genre genre = new Genre(name,slug);
        Genre createdGenre = this.genreRepository.save(genre);

        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE,"Đã tạo chủ đề mới thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse,createdGenre);
        return new ResponseEntity<>(apiDataResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiDataResponse> getListService(String name) {
        List<Genre> genres = this.genreRepository.findListGenres(name);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã lấy danh sách chủ đề thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse,genres);
        return ResponseEntity.ok(apiDataResponse);
    }

    @Override
    public ResponseEntity<ApiDataResponse> updateService(Long id, GenreRequest genreRequest) {
        String name = genreRequest.getName();
        Genre genre = this.genreRepository.findById(id).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE,"Không tìm thấy chủ đề")));
        genre.setName(name);
        genre.setSlug(slugify.slugify(name));
        Genre updatedGenre = this.genreRepository.save(genre);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã cập nhật chủ đề thành công");
        ApiDataResponse apiDataResponse = new ApiDataResponse(apiResponse,updatedGenre);
        return ResponseEntity.ok(apiDataResponse);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteService(Long id) {
        Genre genre = this.genreRepository.findById(id).orElseThrow(() -> new NotFoundRequestException(new ApiResponse(Boolean.FALSE,"Không tìm thấy chủ đề")));
        this.genreRepository.deleteById(id);
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "Đã xóa chủ đề thành công");
        return ResponseEntity.ok(apiResponse);
    }
}

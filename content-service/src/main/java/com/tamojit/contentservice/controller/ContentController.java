package com.tamojit.contentservice.controller;

import com.tamojit.contentservice.dto.MovieRequestDto;
import com.tamojit.contentservice.dto.MovieResponseDto;
import com.tamojit.contentservice.model.Genre;
import com.tamojit.contentservice.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@Slf4j
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    // Add movie to catalogue
    @PostMapping
    public ResponseEntity<MovieResponseDto> addMovie(
        @Valid @RequestBody MovieRequestDto movieRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(contentService.addMovie(movieRequestDto));
    }

    // Get all movies
    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        return ResponseEntity.ok(contentService.getAllMovies());
    }

    // Get all movies by genre
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieResponseDto>> getMoviesByGenre(@PathVariable Genre genre) {
        return ResponseEntity.ok(contentService.getMoviesByGenre(genre));
    }

    // Get movie by ID
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable String movieId) {
        return ResponseEntity.ok(contentService.getMovieById(movieId));
    }

    // Search movies by title
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(contentService.searchMovies(title));
    }
}

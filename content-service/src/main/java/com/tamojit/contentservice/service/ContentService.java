package com.tamojit.contentservice.service;

import com.tamojit.contentservice.dto.MovieRequestDto;
import com.tamojit.contentservice.dto.MovieResponseDto;
import com.tamojit.contentservice.model.Genre;
import com.tamojit.contentservice.model.Movie;
import com.tamojit.contentservice.model.VideoStatus;
import com.tamojit.contentservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {
    private final MovieRepository movieRepository;

    public MovieResponseDto addMovie(MovieRequestDto movieRequestDto) {
        log.info("addMovie({})", movieRequestDto.getTitle());

        Movie movie = new Movie();
        movie.setTitle(movieRequestDto.getTitle());
        movie.setDescription(movieRequestDto.getDescription());
        movie.setGenre(movieRequestDto.getGenre());
        movie.setDirector(movieRequestDto.getDirector());
        movie.setCast(movieRequestDto.getCast());
        movie.setReleaseYear(movieRequestDto.getReleaseYear());
        movie.setRating(movieRequestDto.getRating());
        movie.setThumbnailUrl(movieRequestDto.getThumbnailUrl());
        movie.setDurationMinutes(movieRequestDto.getDurationMinutes());
        movie.setVideoStatus(VideoStatus.PENDING);

        Movie savedMovie = movieRepository.save(movie);
        log.info("Movie added with Id: {}", savedMovie.getId());

        return mapToResponse(savedMovie);
    }

    public List<MovieResponseDto> getAllMovies() {
        return movieRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public List<MovieResponseDto> getMoviesByGenre(Genre genre) {
        return movieRepository.findByGenre(genre)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public MovieResponseDto getMovieById(String movieId) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));

        return mapToResponse(movie);
    }

    public List<MovieResponseDto> searchMovies(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    // updating video key with S3 key for the video after video-service uploads the movie to S3
    public void updateVideoKey(String movieId, String videoKey) {
        log.info("updateVideoKey({}, {})", movieId, videoKey);
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));

        movie.setVideoKey(videoKey);
        movie.setVideoStatus(VideoStatus.UPLOADED);
        movieRepository.save(movie);
    }

    // .ts playlist containing .m3u8 segments has been created for streaming
    public void updateHlsUrl(String movieId, String hlsUrl) {
        log.info("update HLS URL: ({}, {})", movieId, hlsUrl);
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));

        movie.setHlsUrl(hlsUrl);
        movie.setVideoStatus(VideoStatus.READY);
        movieRepository.save(movie);

        log.info("Movie {} is now ready for streaming", movie.getId());
    }

    // Movie -> MovieResponseDTO
    private MovieResponseDto mapToResponse(Movie movie) {
        MovieResponseDto movieResponseDto = new MovieResponseDto();

        movieResponseDto.setId(movie.getId());
        movieResponseDto.setTitle(movie.getTitle());
        movieResponseDto.setDescription(movie.getDescription());
        movieResponseDto.setGenre(movie.getGenre());
        movieResponseDto.setDirector(movie.getDirector());
        movieResponseDto.setCast(movie.getCast());
        movieResponseDto.setReleaseYear(movie.getReleaseYear());
        movieResponseDto.setRating(movie.getRating());
        movieResponseDto.setThumbnailUrl(movie.getThumbnailUrl());
        movieResponseDto.setDurationMinutes(movie.getDurationMinutes());
        movieResponseDto.setVideoKey(movie.getVideoKey());
        movieResponseDto.setVideoStatus(movie.getVideoStatus());
        movieResponseDto.setHlsUrl(movie.getHlsUrl());
        movieResponseDto.setCreatedAt(movie.getCreatedAt());

        return movieResponseDto;
    }
}

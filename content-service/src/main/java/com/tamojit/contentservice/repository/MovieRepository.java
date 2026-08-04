package com.tamojit.contentservice.repository;

import com.tamojit.contentservice.model.Genre;
import com.tamojit.contentservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByGenre(Genre genre);
    List<Movie> findByTitleContainingIgnoreCase(String title); // ignore case of title
}

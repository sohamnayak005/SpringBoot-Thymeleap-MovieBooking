package com.soham.movie_ticket.repsitory;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soham.movie_ticket.dto.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{

	List<Movie> findByReleaseDate(LocalDate movieDate);

}

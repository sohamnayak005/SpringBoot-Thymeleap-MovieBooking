package com.soham.movie_ticket.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soham.movie_ticket.dto.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{

}

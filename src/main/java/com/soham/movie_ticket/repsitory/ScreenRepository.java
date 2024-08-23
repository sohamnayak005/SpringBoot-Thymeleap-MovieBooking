package com.soham.movie_ticket.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soham.movie_ticket.dto.Screen;

public interface ScreenRepository extends JpaRepository<Screen, Integer>  {

	boolean existsByName(String name);
  
}

package com.soham.movie_ticket.repsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soham.movie_ticket.dto.Theatre;

public interface TheatreRepository extends JpaRepository<Theatre, Integer>{
	 public boolean existsByEmail(String email);
	 public boolean existsByMobile(long mobile);
	 public Theatre findByEmail(String email);
	 public Theatre findByMobile(long mobile);
	public List<Theatre> findByApprovedFalseAndVerifiedTrue();
}

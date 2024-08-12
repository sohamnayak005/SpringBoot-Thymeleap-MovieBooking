package com.soham.movie_ticket.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soham.movie_ticket.dto.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer>{
  public boolean existsByEmail(String email);
  public boolean existsByMobile(long mobile);
}

package com.soham.movie_ticket.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Screen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
   String name;
   @Transient
   int row;
   @Transient
   int column;
   
   @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
   List<Seat>seats=new ArrayList<Seat>();
}

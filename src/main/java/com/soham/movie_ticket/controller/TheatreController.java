package com.soham.movie_ticket.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soham.movie_ticket.dto.Customer;
import com.soham.movie_ticket.dto.Movie;
import com.soham.movie_ticket.dto.Screen;
import com.soham.movie_ticket.dto.Seat;
import com.soham.movie_ticket.dto.Show;
import com.soham.movie_ticket.dto.Theatre;
import com.soham.movie_ticket.helper.AES;
import com.soham.movie_ticket.helper.EmailSendingHelper;
import com.soham.movie_ticket.repsitory.CustomerRepository;
import com.soham.movie_ticket.repsitory.MovieRepository;
import com.soham.movie_ticket.repsitory.ScreenRepository;
import com.soham.movie_ticket.repsitory.TheatreRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/theatre")
public class TheatreController {
	
   @Autowired
   Theatre theatre;
   
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	TheatreRepository theatreRepository;
   
	@Autowired
	EmailSendingHelper emailSendingHelper;
	
	@Autowired
	ScreenRepository screenRepository;
	
	@Autowired
	MovieRepository movieRepository;
	
	@GetMapping("/signup")
    public String loadSignup(ModelMap map) {
		map.put("theatre", theatre);
		return "theatre-signup.html";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid Theatre theatre,BindingResult result,HttpSession session) {
		if (!theatre.getPassword().equals(theatre.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "error.confirmPassword", "* Password Missmatch");
		}
		if (customerRepository.existsByEmail(theatre.getEmail())
				|| theatreRepository.existsByEmail(theatre.getEmail())) {
			result.rejectValue("email", "error.email", "* Account Already Exists");
		}
		if (customerRepository.existsByMobile(theatre.getMobile())
				|| theatreRepository.existsByMobile(theatre.getMobile())) {
			result.rejectValue("mobile", "error.mobile", "* Account Already Exists");
		}

		if (result.hasErrors()) {
			return "theatre-signup.html";
		} else {
			theatre.setPassword(AES.encrypt(theatre.getPassword(), "123"));
			theatre.setOtp(new Random().nextInt(100000,1000000));
			emailSendingHelper.sendMailToTheatre(theatre);
			theatreRepository.save(theatre);
			session.setAttribute("success", "Otp Sent Success!!!");
			session.setAttribute("id",theatre.getId());
			return "redirect:/theatre/enter-otp";
		}
		
	}
	
	@GetMapping("/enter-otp")
	public String enterOtp(ModelMap map) {
		map.put("user", "theatre");
		 return "enter-otp.html";
	}
	
	@PostMapping("/verify-otp")
	public String enterOtp(@RequestParam int id,@RequestParam int otp,HttpSession session) {
		 Theatre theatre=theatreRepository.findById(id).orElseThrow();
		   if(theatre.getOtp()==otp) {
			   theatre.setVerified(true);
			   theatreRepository.save(theatre);
			   session.setAttribute("success", "Account Created Success");
				return "redirect:/login";
		   }
		   else {
				session.setAttribute("failure", "Invalid OTP! Try Again");
				return "redirect:/theatre/enter-otp";
			}
		  
	}
	
	@GetMapping("/add-screen")
	public String addScreen(HttpSession session) {
		if(session.getAttribute("theatre")!=null)
		{
			return "add-screen.html";
		}
		else {
			session.setAttribute("failure", "Invalid session, Login again");
			return "redirect:/login";
		}
		
	}
	
	@PostMapping("/add-screen")
	public String addScreen(Screen screen,HttpSession session) {
		Theatre theatre=(Theatre)session.getAttribute("theatre");
		
		if(theatre!=null) {
			if(screenRepository.existsByName(screen.getName())) {
				session.setAttribute("failure","Screen Already Exists");
				return "redirect:/";
			}else {
				
				//Creating all seats
				List<Seat>seats=new ArrayList<>();
				for(char i='A';i<'A'+screen.getRow();i++) {
					for(int j=1;j<=screen.getColumn();j++) {
						Seat seat=new Seat();
						seat.setSeatNumber(i+""+j);
						seats.add(seat);
					}
				}
				
				screen.setSeats(seats);
//				System.out.println(seats);
				List<Screen>screens=theatre.getScreens();
				System.out.println(screens);
				screens.add(screen);
				System.out.println(screens);
				theatreRepository.save(theatre);
				session.setAttribute("theatre", theatreRepository.findById(theatre.getId()).orElseThrow());
				session.setAttribute("success", "Screen and Seats Added Success");
				return "redirect:/";
			}
		}else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/add-show")
	public String addShow(HttpSession session,ModelMap map) {
		Theatre theatre=(Theatre)session.getAttribute("theatre");
		if(theatre!=null) {
			List<Screen>screens=theatre.getScreens();
			List<Movie>movies=movieRepository.findAll();
			if (screens.isEmpty()) {
				session.setAttribute("failure", "No Screens Available for Adding Show");
				return "redirect:/";
			}
			if (movies.isEmpty()) {
				session.setAttribute("failure", "No Movies Available for Adding Show");
				return "redirect:/";
			}
			map.put("screens", screens);
			map.put("movies", movies);
			return "add-show.html";
		}
		else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}
	
	@PostMapping("/add-show")	
	public String addShow(HttpSession session,Show show) {
		
		Theatre theatre=(Theatre)session.getAttribute("theatre");
		if(theatre!=null) {
			System.out.println(show);
			show.setMovie(movieRepository.findById(show.getMovie().getId()).orElseThrow());
			show.setScreen(screenRepository.findById(show.getScreen().getId()).orElseThrow());
			System.out.println(show);
			return "redirect:/login";
		}
		else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
		
		
	}
	
}


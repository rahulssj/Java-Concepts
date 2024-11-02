package com.microservices.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.demo.dto.User;
import com.microservices.demo.repositories.UserRepository;
import com.microservices.demo.services.CustomUserDetailsServiceImpl;
import com.microservices.demo.services.UserService;

@RestController
public class RestApiController {
	@Autowired
	UserService userService;
@Autowired
	UserRepository userrepo;
@Autowired
PasswordEncoder passwordEncoder;


@GetMapping("/users")
public ResponseEntity<?> getAllUsers() {
List<User> users=userrepo.findAll() ;


	return ResponseEntity.ok(users);
}


@GetMapping("/users/{id}")
	public User getusers(@PathVariable String id) {
User user=userrepo.findById(id).get();


		return user;
	}

	@PostMapping("/users")
	public ResponseEntity<?> saveusers(@RequestBody User user) {
System.out.println(user);
user.setPassword(passwordEncoder.encode(user.getPassword()));
		return ResponseEntity.ok(userrepo.save(user));
	}
	
	@PutMapping("/users/")
	public ResponseEntity<?> updateusers(@RequestBody User user) {
System.out.println(user);
user.setPassword(passwordEncoder.encode(user.getPassword()));
		return ResponseEntity.ok(userrepo.save(user));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user){
		
		return ResponseEntity.ok(userService.verify(user));
	
	}
}


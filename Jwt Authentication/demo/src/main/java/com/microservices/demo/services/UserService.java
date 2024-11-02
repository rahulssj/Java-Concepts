package com.microservices.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.microservices.demo.dto.User;

@Service
public class UserService {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JWTUtlilService jwt;

	public String verify(User user) {
Authentication  auth=		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	if(auth.isAuthenticated()) {
		return jwt.generateToken(user.getUsername());
	}
	
	return "Fail";
	}

}

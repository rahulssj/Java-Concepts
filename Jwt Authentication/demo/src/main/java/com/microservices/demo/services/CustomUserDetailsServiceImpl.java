package com.microservices.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservices.demo.dto.User;
import com.microservices.demo.repositories.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userrepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=userrepo.findByUsername(username);
		System.out.println("user==>"+user);
		if(user!=null) {
			return org.springframework.security.core.userdetails.User.builder()
			.username(user.getUsername())
			.password(user.getPassword())
			.roles(user.getRoles().toArray(new String[0])).build();
		}
		throw new UsernameNotFoundException(username);
	}

}

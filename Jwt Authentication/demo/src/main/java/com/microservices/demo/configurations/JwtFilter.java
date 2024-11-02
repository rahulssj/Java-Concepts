package com.microservices.demo.configurations;

import java.io.IOException;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.microservices.demo.services.CustomUserDetailsServiceImpl;
import com.microservices.demo.services.JWTUtlilService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	JWTUtlilService jwt;
	@Autowired
	ApplicationContext applicationCtx;
	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	String headerString=request.getHeader("Authorization");	
	String token=null;
	String username=null;
	if(headerString!=null && headerString.startsWith("Bearer ")) {
		token=headerString.substring(7);
		username=jwt.extractUserName(token);
	}
	if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null ) {
		UserDetails  userdetails=customUserDetailsServiceImpl.loadUserByUsername(username);
	if(jwt.validateToken(token,userdetails)) {
		UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userdetails, null,userdetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
			}
	
	}
	
	filterChain.doFilter(request, response);
	}

}

package com.microservices.demo.configurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservices.demo.services.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig {
    
	@Autowired
	@Lazy
	private CustomUserDetailsServiceImpl userDetailService;

	@Autowired
	private JwtFilter jwtFilter;
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request->{
					request.requestMatchers("/v2/**","/login").permitAll();

					request.requestMatchers("/users/**").authenticated();
		
				request.anyRequest().authenticated();
				
		}).httpBasic(Customizer.withDefaults())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	
	}
	@Bean
	public UserDetailsService userDetailsService(AuthenticationManagerBuilder auth) throws Exception {
	
	return userDetailService;
	}
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticProvider=new DaoAuthenticationProvider();
		authenticProvider.setUserDetailsService(userDetailService);
		authenticProvider.setPasswordEncoder(passwordEncoder());
	return authenticProvider;}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		
		return config.getAuthenticationManager();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

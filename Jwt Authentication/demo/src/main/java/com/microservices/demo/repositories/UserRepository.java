package com.microservices.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.demo.dto.User;
@Repository
public interface UserRepository extends JpaRepository<User, String>{

	User findByUsername(String username);
}

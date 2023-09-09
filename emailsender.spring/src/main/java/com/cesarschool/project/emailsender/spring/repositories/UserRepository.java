package com.cesarschool.project.emailsender.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesarschool.project.emailsender.spring.entities.User;

import jakarta.websocket.server.PathParam;

public interface UserRepository extends JpaRepository<User, String>{
	
	User findByEmail(@PathParam("email") String email);
	
}
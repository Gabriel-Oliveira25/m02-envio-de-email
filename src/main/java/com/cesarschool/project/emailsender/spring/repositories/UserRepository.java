package com.cesarschool.project.emailsender.spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesarschool.project.emailsender.spring.entities.User;

import jakarta.websocket.server.PathParam;

public interface UserRepository extends JpaRepository<User, String>{
	
	User findByEmail(@PathParam("email") String email);
	
	User findByName(@PathParam("name") String name);
	
	List<User> findByOrganization(@PathParam("organization") String organization);
	
}
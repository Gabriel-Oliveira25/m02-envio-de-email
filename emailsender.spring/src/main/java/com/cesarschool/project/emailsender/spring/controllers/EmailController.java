package com.cesarschool.project.emailsender.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.project.emailsender.spring.dto.request.EmailRequestDTO;
import com.cesarschool.project.emailsender.spring.dto.response.GenericResponseDTO;
import com.cesarschool.project.emailsender.spring.services.EmailServices;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/email")
public class EmailController {
	@Autowired
	private final EmailServices service;

	@PostMapping
	public ResponseEntity<GenericResponseDTO> sendMail(@RequestBody EmailRequestDTO request){
		return new ResponseEntity<GenericResponseDTO>(service.sendEmail(request), HttpStatus.CREATED);
	}
	
	@PostMapping("/{id}/{name}")
	public ResponseEntity<GenericResponseDTO> sendMessageToUser(@PathVariable("id") String idMessage, @PathVariable("name") String idUser){
		return new ResponseEntity<GenericResponseDTO>(service.sendMessage(idMessage, idUser), HttpStatus.OK);
	}
	
	
}

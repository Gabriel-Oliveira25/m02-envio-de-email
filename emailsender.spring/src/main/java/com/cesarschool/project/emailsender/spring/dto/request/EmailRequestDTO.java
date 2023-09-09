package com.cesarschool.project.emailsender.spring.dto.request;

import com.cesarschool.project.emailsender.spring.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequestDTO {

	@NotBlank
	private User user;

	@Email
	@NotBlank
	private String sendFrom;

	@Email
	@NotBlank
	private String sendTo;
	
	@NotBlank
	private String subject;
	
	@NotBlank
	private String message;

}
package com.cesarschool.project.emailsender.spring.services;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cesarschool.project.emailsender.spring.dto.request.EmailRequestDTO;
import com.cesarschool.project.emailsender.spring.dto.response.GenericResponseDTO;
import com.cesarschool.project.emailsender.spring.entities.Email;
import com.cesarschool.project.emailsender.spring.enums.StatusMail;
import com.cesarschool.project.emailsender.spring.exceptions.GeneralException;
import com.cesarschool.project.emailsender.spring.repositories.EmailRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailServices {
	private final EmailRepository repositoryEmail;

	private final JavaMailSender mailSender;

	public GenericResponseDTO sendEmail(EmailRequestDTO request) {
		
		try {
			SimpleMailMessage email = new SimpleMailMessage();
			email.setFrom(request.getSendFrom());
			email.setTo(request.getSendTo());
			email.setSubject(request.getSubject());
			email.setText(request.getMessage());
			mailSender.send(email);
			request.setStatusMail(StatusMail.SENT);

		} catch (MailException e) {
			request.setStatusMail(StatusMail.ERROR);
			throw new GeneralException("Falha no envio", HttpStatus.BAD_REQUEST);
		} finally {
			Email entity = new Email();
			BeanUtils.copyProperties(request, entity);
			repositoryEmail.save(entity);
		}		
		return GenericResponseDTO.builder().message("Alo").status(HttpStatus.OK).build();
	}

}
package com.cesarschool.project.emailsender.spring.services;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cesarschool.project.emailsender.spring.dto.request.EmailRequestDTO;
import com.cesarschool.project.emailsender.spring.dto.response.GenericResponseDTO;
import com.cesarschool.project.emailsender.spring.entities.Email;
import com.cesarschool.project.emailsender.spring.entities.Message;
import com.cesarschool.project.emailsender.spring.entities.User;
import com.cesarschool.project.emailsender.spring.enums.StatusMail;
import com.cesarschool.project.emailsender.spring.exceptions.GeneralException;
import com.cesarschool.project.emailsender.spring.repositories.EmailRepository;
import com.cesarschool.project.emailsender.spring.repositories.MessageRepository;
import com.cesarschool.project.emailsender.spring.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailServices {

	private final MessageRepository messageRepository;

	private final UserRepository userRepository;

	private final EmailRepository emailRepository;

	private final JavaMailSender mailSender;

	public GenericResponseDTO sendEmail(EmailRequestDTO request) {

		try {
			SimpleMailMessage email = new SimpleMailMessage();
			email.setFrom(request.getSendFrom());
			email.setTo(request.getSendTo());
			email.setSubject(request.getSubject());
			email.setText(request.getText());
			mailSender.send(email);
			request.setStatusMail(StatusMail.SENT);

		} catch (MailException e) {
			request.setStatusMail(StatusMail.ERROR);
			throw new GeneralException("Falha no envio", HttpStatus.BAD_REQUEST);
		} finally {
			Email entity = new Email();
			BeanUtils.copyProperties(request, entity);
			emailRepository.save(entity);
		}
		return GenericResponseDTO.builder().message("Alo").status(HttpStatus.OK).build();
	}

	
	public GenericResponseDTO sendMessage(String idMessage, String userName) {

		Email e = new Email();

		Optional.ofNullable(messageRepository.findById(idMessage)).ifPresentOrElse(message -> {
			Optional.ofNullable(userRepository.findByName(userName)).ifPresentOrElse(user -> {
				Message m = messageRepository.findById(idMessage).orElse(null);
				User u = userRepository.findByName(userName);
				try {
					SimpleMailMessage email = new SimpleMailMessage();
					email.setFrom("emailsendernext@gmail.com");
					email.setTo(u.getEmail());
					email.setSubject(m.getSubject());
					email.setText(m.getText());

					mailSender.send(email);

					e.setStatusMail(StatusMail.SENT);
 
				} catch (MailException exception) {
					e.setStatusMail(StatusMail.ERROR);
					throw new GeneralException("Falha no envio", HttpStatus.BAD_REQUEST);
				} finally {

					e.setUser(u);
					e.setSendFrom("emailsendernext@gmail.com");
					e.setSendTo(u.getEmail());
					e.setSubject(m.getSubject());
					e.setText(m.getText());
					emailRepository.save(e);
				}
			}, () -> {
				throw new GeneralException("Usuário não encontrada no banco de dados", HttpStatus.NOT_FOUND);
			});
		}, () -> {

			throw new GeneralException("Mensagem não encontrada no banco de dados", HttpStatus.NOT_FOUND);
		});

		return GenericResponseDTO.builder().message("Enviado com sucesso").status(HttpStatus.OK).build();

	}

}

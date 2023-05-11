package org.example.service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String username;

    @Test
    public void testSendMessage() {
        String to = "test@gmail.com";
        String subject = "Test email";
        String text = "This is a test email";

        emailService.sendMessage(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender, Mockito.times(1)).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();
        assertEquals(username, message.getFrom());
        assertEquals(to, Objects.requireNonNull(message.getTo())[0]);
        assertEquals(subject, message.getSubject());
        assertEquals(text, message.getText());
    }
}
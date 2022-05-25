package com.greenfoxacademy.springwebapp.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Value("${spring.mail.username}")
  private String messageFrom;
  @Value("${app.url}")
  private String url;

  private final JavaMailSender javaMailSender;

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendMessage(String email, String activationCode) {
    SimpleMailMessage message = null;

    try {
      message = new SimpleMailMessage();
      message.setFrom(messageFrom);
      message.setTo(email);
      message.setSubject("Sikeres regisztrálás");
      message.setText("Kedves Felhasználó!\n\n"
          + "Az alábbi linken tudod aktiválni profilodat:\n"
          + "link: " + url + activationCode + " \n"
          + "\n\n Üdvözlettel: CH4-Tribes ");
    } catch (Exception e) {
      //TODO
    }
  }






}

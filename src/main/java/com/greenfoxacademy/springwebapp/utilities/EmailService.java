package com.greenfoxacademy.springwebapp.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  //@Value("${app.url}")
  private String url = "localhost:8080/activation/";

  private final JavaMailSenderImpl javaMailSenderImpl;

  @Autowired
  public EmailService(JavaMailSenderImpl javaMailSenderImpl) {
    this.javaMailSenderImpl = javaMailSenderImpl;
  }

  public void sendMessage(String email, String activationCode) {
    SimpleMailMessage message = null;

    try {
      message = new SimpleMailMessage();
      message.setFrom(javaMailSenderImpl.getUsername());
      message.setTo(email);
      message.setSubject("Sikeres regisztrálás");
      message.setText("Kedves Felhasználó!\n\n"
          + "Az alábbi linken tudod aktiválni profilodat:\n"
          + "link: " + url + activationCode + " \n"
          + "\n\n Üdvözlettel: CH4-Tribes ");
      javaMailSenderImpl.send(message);
    } catch (Exception e) {
      //TODO
      System.out.println(e);
    }
  }






}

package com.greenfoxacademy.springwebapp.utilities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class AppConfig {

  //@Value("${spring.mail.username}")
  private String messageFrom = "greenfox.ch4@gmail.com";
  //@Value("${spring.mail.host}")
  private String host = "smtp.gmail.com";
  //@Value("${spring.mail.port}")
  private int port = 587;
  //@Value("${spring.mail.password}")
  private String password = "veryverysecret";
  //@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private boolean isStartTlsEnable = true;

  @Bean
  public JavaMailSenderImpl mailSender() {
    JavaMailSenderImpl jms = new JavaMailSenderImpl();
    jms.setHost(host);
    jms.setPort(port);
    jms.setUsername(messageFrom);
    jms.setPassword(password);

    Properties props = jms.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return jms;
  }
}

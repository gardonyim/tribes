package com.greenfoxacademy.springwebapp.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class EmailConfig {

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
  //@Value("${spring.mail.properties.mail.smtp.auth}")
  private String mailServerAuth = "true";
  //@Value("${spring.mail.templates.path}")
  //Our templates are in the main/resources/mail-templates directory
  private String mailTemplatesPath = "mail-templates";


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

  @Primary
  @Bean
  public ITemplateResolver thymeleafClassLoaderTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setPrefix(mailTemplatesPath + "/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode("HTML");
    templateResolver.setCharacterEncoding("UTF-8");
    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    templateEngine.setTemplateEngineMessageSource(emailMessageSource());
    return templateEngine;
  }

  // from: src/main/resources/mailMessages_xx_YY.properties
  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("mailMessages");
    return messageSource;
  }

}

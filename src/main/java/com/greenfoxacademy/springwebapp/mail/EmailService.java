package com.greenfoxacademy.springwebapp.mail;

import com.greenfoxacademy.springwebapp.exceptions.EmailSendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

  private final JavaMailSenderImpl javaMailSenderImpl;
  private final SpringTemplateEngine thymeleafTemplateEngine;

  @Value("${email.resource.path}")
  private Resource resourceFile;
  @Value("${server.port}")
  private Integer port;
  private String subject = "Sikeres regisztrálás";

  @Autowired
  public EmailService(JavaMailSenderImpl javaMailSenderImpl, SpringTemplateEngine thymeleafTemplateEngine) {
    this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    this.javaMailSenderImpl = javaMailSenderImpl;
  }

  public void sendMessageUsingThymeleafTemplate(String to, String userName, String activationCode) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("recipientName", userName);
    templateModel.put("link", getUrl() + activationCode);

    Context thymeleafContext = new Context();
    thymeleafContext.setVariables(templateModel);

    String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);
    try {
      sendHtmlMessage(to, subject, htmlBody, activationCode);
    } catch (Exception e) {
      throw new EmailSendingException();
    }
  }

  public String getUrl() {
    try {
      return String.format("http://%s:%d/activation/", InetAddress.getLocalHost().getHostAddress(), port);
    } catch (UnknownHostException e) {
      throw new EmailSendingException();
    }
  }

  private String createPlainTextMessageBody(String activationCode) {
    return "Kedves Felhasználó!\n\n"
        + "Az alábbi linken tudod aktiválni profilodat:\n"
        + "link: " + getUrl() + activationCode + " \n"
        + "\n\n Üdvözlettel: CH4-Tribes ";
  }

  private void sendHtmlMessage(String to, String subject, String htmlBody, String activationCode)
          throws MessagingException, UnknownHostException {
    MimeMessage message = javaMailSenderImpl.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(javaMailSenderImpl.getUsername());
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(createPlainTextMessageBody(activationCode), htmlBody);
    helper.addInline("attachment.png", resourceFile);
    javaMailSenderImpl.send(message);
  }

}

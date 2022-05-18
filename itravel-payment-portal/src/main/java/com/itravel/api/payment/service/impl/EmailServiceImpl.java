package com.itravel.api.payment.service.impl;

import com.itravel.api.payment.payload.EmailOutGoingRequest;
import com.itravel.api.payment.service.EmailService;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender emailSender;

  private final SpringTemplateEngine templateEngine;

  @Override
  public void sendPaymentOrderEmail(EmailOutGoingRequest mail, String emailTemplate) throws MessagingException {

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message,
        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());
    Context context = new Context();
    context.setVariables(mail.getProps());
    String html = templateEngine.process(emailTemplate, context);
    helper.setTo(mail.getMailTo());
    helper.setText(html, true);
    helper.setSubject(mail.getSubject());
    helper.setFrom("itravel.fu@gmail.com");
    emailSender.send(message);

  }
}
package com.itravel.api.payment.service;

import com.itravel.api.payment.payload.EmailOutGoingRequest;
import javax.mail.MessagingException;

public interface EmailService {

  void sendPaymentOrderEmail(EmailOutGoingRequest mail, String emailTemplate) throws MessagingException;
}

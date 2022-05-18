package com.itravel.api.payment.service;

import com.itravel.api.payment.enums.PaypalPaymentIntent;
import com.itravel.api.payment.enums.PaypalPaymentMethod;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaypalService {

  Payment createPayment(
      Double total,
      String currency,
      PaypalPaymentMethod method,
      PaypalPaymentIntent intent,
      String description,
      String cancelUrl,
      String successUrl) throws PayPalRESTException;

  Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}

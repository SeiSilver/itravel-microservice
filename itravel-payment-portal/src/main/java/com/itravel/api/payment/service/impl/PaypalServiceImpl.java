package com.itravel.api.payment.service.impl;

import com.itravel.api.payment.enums.PaypalPaymentIntent;
import com.itravel.api.payment.enums.PaypalPaymentMethod;
import com.itravel.api.payment.service.PaypalService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaypalServiceImpl implements PaypalService {

  @Value("${paypal.client.app}")
  private String clientId;
  @Value("${paypal.client.secret}")
  private String clientSecret;
  @Value("${paypal.mode}")
  private String mode;

  private APIContext apiContext;

  @Override
  public Payment createPayment(
      Double total,
      String currency,
      PaypalPaymentMethod method,
      PaypalPaymentIntent intent,
      String description,
      String cancelUrl,
      String successUrl) throws PayPalRESTException {

    Amount amount = new Amount();
    amount.setCurrency(currency);
    amount.setTotal(String.format("%.2f", total));
    Transaction transaction = new Transaction();
    transaction.setDescription(description);
    transaction.setAmount(amount);
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);
    Payer payer = new Payer();
    payer.setPaymentMethod(method.toString());
    Payment payment = new Payment();
    payment.setIntent(intent.toString());
    payment.setPayer(payer);
    payment.setTransactions(transactions);
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl(cancelUrl);
    redirectUrls.setReturnUrl(successUrl);
    payment.setRedirectUrls(redirectUrls);
    apiContext = createAPIContext();
    apiContext.setMaskRequestId(true);
    return payment.create(apiContext);
  }

  @Override
  public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
    Payment payment = new Payment();
    payment.setId(paymentId);
    PaymentExecution paymentExecute = new PaymentExecution();
    paymentExecute.setPayerId(payerId);
    return payment.execute(apiContext, paymentExecute);
  }

  private APIContext createAPIContext() throws PayPalRESTException {
    Map<String, String> sdkConfig = new HashMap<>();
    sdkConfig.put("mode", mode);
    APIContext newApiContext = new APIContext(new OAuthTokenCredential(clientId, clientSecret, sdkConfig).getAccessToken());
    newApiContext.setConfigurationMap(sdkConfig);
    return newApiContext;
  }
}
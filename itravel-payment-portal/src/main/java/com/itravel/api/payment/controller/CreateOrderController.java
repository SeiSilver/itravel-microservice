package com.itravel.api.payment.controller;

import com.itravel.api.payment.exception.InvalidJsonException;
import com.itravel.api.payment.exception.MissingRequireFieldException;
import com.itravel.api.payment.payload.CreateOrderRequest;
import com.itravel.api.payment.service.OrderService;
import com.itravel.api.payment.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class CreateOrderController {

  private final OrderService orderService;
  private final PayloadUtils payloadUtils;

  public void process(String payload) {

    CreateOrderRequest request;
    try {
      request = payloadUtils.parseJson(payload, CreateOrderRequest.class);
      payloadUtils.validateRequiredFields(request);
      orderService.createOrder(request);

    } catch (MissingRequireFieldException | InvalidJsonException e) {
      log.error("error: ", e);
    }
  }

}

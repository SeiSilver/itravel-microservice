package com.itravel.api.payment.controller;

import com.itravel.api.payment.config.ApplicationProperties;
import com.itravel.api.payment.controller.endpoint.AuthPortalEndPoint;
import com.itravel.api.payment.enums.OrderStatus;
import com.itravel.api.payment.enums.PaypalPaymentIntent;
import com.itravel.api.payment.enums.PaypalPaymentMethod;
import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.dto.BasicResponse;
import com.itravel.api.payment.model.dto.PaypalResponse;
import com.itravel.api.payment.service.OrderService;
import com.itravel.api.payment.service.PaypalService;
import com.itravel.api.payment.util.CommonUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.annotations.ApiOperation;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping(AuthPortalEndPoint.PAYPAL)
@RequiredArgsConstructor
@Log4j2
public class PaypalPaymentController {

  private static final String URL_PAYPAL_SUCCESS = AuthPortalEndPoint.PAYPAL + AuthPortalEndPoint.PAYPAL_PAY_SUCCESS;
  private static final String URL_PAYPAL_CANCEL = AuthPortalEndPoint.PAYPAL + AuthPortalEndPoint.PAYPAL_PAY_CANCEL;

  private static final String REDIRECT_FAIL_URL = "/pay/default/fail";
  private static final String REDIRECT_SUCCESS_URL = "/pay/default/success";
  private static final String REDIRECT = "redirect:";

  private final PaypalService paypalService;
  private final OrderService orderService;
  private final ApplicationProperties applicationProperties;

  @ResponseBody
  @PostMapping(AuthPortalEndPoint.PAYPAL_PAY)
  @ApiOperation(value = "User pay with VND")
  public PaypalResponse pay(
      @RequestParam(value = "redirectUrl", required = false,
          defaultValue = AuthPortalEndPoint.PAYPAL + REDIRECT_SUCCESS_URL) String redirectUrl,
      @RequestParam("orderBillId") String orderBillId) throws ApplicationException {
    var orderEntity = orderService.updateOrderStatusByOrderBillId(orderBillId, OrderStatus.PROCESSING);

    String baseUrl = applicationProperties.getPaymentPortal().getBaseUrl();
    String cancelUrl = baseUrl + URL_PAYPAL_CANCEL;
    String successUrl = baseUrl +
        URL_PAYPAL_SUCCESS + "?redirectUrl=" + redirectUrl + "&orderBillId=" + orderBillId;
    if (orderEntity.getTotalPrice() < 10000) {
      throw new ApplicationException(54, "Total Amount must greater than 10000");
    }
    double totalAmount = CommonUtils.convertVNDToUSD(orderEntity.getTotalPrice());
    try {
      Payment payment = paypalService.createPayment(
          totalAmount,
          "USD",
          PaypalPaymentMethod.PAYPAL,
          PaypalPaymentIntent.SALE,
          "payment description",
          cancelUrl,
          successUrl);
      for (Links links : payment.getLinks()) {
        if (links.getRel().equals("approval_url")) {
          return PaypalResponse.builder()
              .payUrl(links.getHref())
              .message("Paypal URL for order with orderBillId = " + orderBillId)
              .build();
        }
      }
    } catch (PayPalRESTException e) {
      log.error(e.getMessage(), e);
    }
    return PaypalResponse.builder()
        .payUrl(null)
        .message("Fail to gen Paypal URL for order with orderBillId = " + orderBillId)
        .build();
  }

  @ResponseBody
  @GetMapping(AuthPortalEndPoint.PAYPAL_PAY_CANCEL)
  @ApiIgnore
  public String cancelPay() {
    return "cancel";
  }

  @GetMapping(AuthPortalEndPoint.PAYPAL_PAY_SUCCESS)
  @ApiIgnore
  public String successPay(@RequestParam("paymentId") String paymentId,
      @RequestParam("PayerID") String payerId,
      @RequestParam("redirectUrl") String redirectUrl,
      @RequestParam("orderBillId") String orderBillId) throws ApplicationException {
    try {
      Payment payment = paypalService.executePayment(paymentId, payerId);
      if (payment.getState().equals("approved")) {
        orderService.updateOrderStatusByOrderBillId(orderBillId, OrderStatus.FINISHED);
        orderService.sendEmail(orderBillId);
        return REDIRECT + redirectUrl;
      }
    } catch (PayPalRESTException | MessagingException e) {
      log.error(e.getMessage(), e);
    }
    return REDIRECT + REDIRECT_FAIL_URL;
  }

  @ResponseBody
  @GetMapping(REDIRECT_SUCCESS_URL)
  @ApiIgnore
  public BasicResponse defaultSuccessUrl() {
    return BasicResponse.builder().code("200").message("payment success").build();
  }

  @ResponseBody
  @GetMapping(REDIRECT_FAIL_URL)
  @ApiIgnore
  public BasicResponse defaultFailUrl() {
    return BasicResponse.builder().code("200").message("payment fail").build();
  }

}

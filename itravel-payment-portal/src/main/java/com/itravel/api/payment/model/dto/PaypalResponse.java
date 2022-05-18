package com.itravel.api.payment.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaypalResponse {

  private String payUrl;

  private String message;

}

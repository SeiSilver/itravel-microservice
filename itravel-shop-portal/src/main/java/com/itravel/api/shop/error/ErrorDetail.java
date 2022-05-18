package com.itravel.api.shop.error;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ErrorDetail {

  private Integer errorCode;
  private String errorMessage;

}


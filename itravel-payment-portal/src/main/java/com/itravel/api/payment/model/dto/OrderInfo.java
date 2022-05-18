package com.itravel.api.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

  private Integer id;
  private String orderBillId;
  private Long accountId;
  private String fullName;
  private String phoneNumber;
  private Integer totalPrice;

}

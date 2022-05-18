package com.itravel.api.shop.model.dto;

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

  private String orderBillId;
  private Integer accountId;
  private String fullName;
  private String phoneNumber;
  private Long totalPrice;

}

package com.itravel.api.payment.model.dto;

import com.itravel.api.payment.enums.RefundStatus;
import java.time.ZonedDateTime;
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
public class RefundRequestDetailDto {

  private Integer id;

  private Integer orderItemId;

  private String orderBillId;

  private String userName;

  private String shopName;

  private String reportType;

  private String requestReason;

  private String refuseReason;

  private RefundStatus status;

  private ZonedDateTime orderItemCreatedAt;
  private ZonedDateTime usedStart;
  private ZonedDateTime usedEnd;
  private ZonedDateTime createdAt;

}

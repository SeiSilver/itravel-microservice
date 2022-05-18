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
public class RefundRequestDto {

  private Integer id;

  private Long accountId;

  private Integer orderItemId;

  private String orderBillId;

  private String reportType;

  private String requestReason;

  private String refuseReason;

  private RefundStatus status;

  private ZonedDateTime orderItemCreatedAt;

  private ZonedDateTime createdAt;

}

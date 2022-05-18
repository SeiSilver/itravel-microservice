package com.itravel.api.payment.payload;

import javax.validation.constraints.NotNull;
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
public class CreateRefundRequest {

  @NotNull
  private Long accountId;

  @NotNull
  private Integer orderItemId;

  @NotNull
  private String reportType;

  @NotNull
  private String reason;

}

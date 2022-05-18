package com.itravel.api.account.payload;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateScoreRequest {

  private String comment;

  @Min(0)
  @Max(5)
  private Integer ratePoint;

  @NotNull
  @Min(1)
  private Integer mainServiceId;

}

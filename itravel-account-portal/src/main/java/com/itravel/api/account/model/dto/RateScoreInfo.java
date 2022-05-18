package com.itravel.api.account.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateScoreInfo {

  private Integer id;
  @JsonIgnore
  private Long accountId;

  private AccountBasicInfo account;
  private Integer mainServiceId;
  private String comment;
  private Integer ratePoint;
  private ZonedDateTime createdAt;

}

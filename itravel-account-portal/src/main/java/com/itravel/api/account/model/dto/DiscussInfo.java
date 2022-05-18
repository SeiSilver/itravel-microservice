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
public class DiscussInfo {

  private Integer id;
  @JsonIgnore
  private Long accountId;
  private AccountBasicInfo account;
  private String message;
  private Integer mainServiceId;
  private Integer discussParentId;
  private ZonedDateTime createdAt;

}

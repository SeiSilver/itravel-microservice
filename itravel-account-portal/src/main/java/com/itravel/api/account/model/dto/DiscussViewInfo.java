package com.itravel.api.account.model.dto;

import java.time.ZonedDateTime;
import java.util.List;
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
public class DiscussViewInfo {

  private Integer id;
  private AccountBasicInfo account;
  private String message;
  private ZonedDateTime createdAt;
  private List<DiscussViewInfo> discussReply;

}

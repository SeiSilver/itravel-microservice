package com.itravel.api.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountBasicInfo {

  private Long id;
  private String fullName;
  private String email;
  private String imageLink;

}

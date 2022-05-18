package com.itravel.api.portal.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountPublicInfo {

  private Long id;

  private String fullName;

  private String email;

  private String imageLink;

}

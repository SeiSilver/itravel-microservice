package com.itravel.api.portal.auth.model.dto;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {

  private String accessToken;

  private String tokenType;

  private String email;

  private ZonedDateTime expiredDate;

  private ZonedDateTime issuedDate;

}

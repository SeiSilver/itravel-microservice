package com.itravel.api.shop.model.dto;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

  private String accessToken;

  private String tokenType;

  private String email;

  @DateTimeFormat
  private ZonedDateTime expiredDate;

  @DateTimeFormat
  private ZonedDateTime issuedDate;

}

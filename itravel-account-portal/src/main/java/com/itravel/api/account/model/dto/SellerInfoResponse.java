package com.itravel.api.account.model.dto;

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
public class SellerInfoResponse {

  private Integer id;

  private String nationalIdNumber;

  private String nationality;

  private String placeOfOrigin;

  private String placeOfResidence;

  private String nationalIdCardImageFront;

  private String nationalIdCardImageBack;

  private Boolean isVerified;

  private ZonedDateTime modifiedAt;

  private ZonedDateTime createdAt;

}

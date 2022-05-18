package com.itravel.api.account.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SellerInfoRequest {

  @NotBlank(message = "Must not empty")
  @NotNull(message = "Must not empty")
  private String nationalIdNumber;

  @NotBlank(message = "Must not empty")
  @NotNull(message = "Must not empty")
  private String nationality;

  @NotBlank(message = "Must not empty")
  @NotNull(message = "Must not empty")
  private String placeOfOrigin;

  @NotBlank(message = "Must not empty")
  @NotNull(message = "Must not empty")
  private String placeOfResidence;

  @NotBlank(message = "Must not empty")
  @NotNull(message = "Must not empty")
  private String nationalIdCardImageFrontBase64;

  @NotBlank(message = "Must not empty")
  @NotNull(message = "Must not empty")
  private String nationalIdCardImageBackBase64;

}

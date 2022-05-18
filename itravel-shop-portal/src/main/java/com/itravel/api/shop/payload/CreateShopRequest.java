package com.itravel.api.shop.payload;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateShopRequest {
  @NotNull
  private String shopName;

  @NotNull
  private String address;

  @NotNull
  private String description;
}

package com.itravel.api.shop.payload;

import com.itravel.api.shop.enums.ShopStatus;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UpdateShopRequest {

  @NotNull
  private String shopName;

  @NotNull
  private String address;

  @NotNull
  private String phoneNumber;

  @NotNull
  private String description;

  @NotNull
  private ShopStatus status;
}

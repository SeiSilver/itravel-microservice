package com.itravel.api.shop.model.dto;

import com.itravel.api.shop.enums.ShopStatus;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShopInfo {
  private Integer id;
  private Integer ownerId;
  private String ownerName;
  private String shopName;
  private String icon;
  private String address;
  private String phoneNumber;
  private String description;
  private ShopStatus status;
  private ZonedDateTime createdAt;
  private List<FullServiceInfo> services;
}

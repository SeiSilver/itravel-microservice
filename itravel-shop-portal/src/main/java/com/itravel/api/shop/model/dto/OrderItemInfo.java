package com.itravel.api.shop.model.dto;

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
public class OrderItemInfo {

  private Integer id;

  private Integer subServiceId;

  private Integer mainServiceId;

  private Integer shopId;

  private String shopName;

  private String mainServiceTitle;

  private String subServiceTitle;

  private Integer quantity;

  private Long price;

  private String image;

  private ZonedDateTime usedStart;

  private ZonedDateTime usedEnd;

}

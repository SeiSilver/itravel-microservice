package com.itravel.api.shop.model.dto;

import lombok.*;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CartItemInfo {
    private Integer id;
    private String mainServiceTitle;
    private String subServiceTitle;
    private String image;
    private String mainServiceShopShopName;
    private Integer quantity;
    private Long price;
    private ZonedDateTime usedStart;
    private ZonedDateTime usedEnd;
    private Integer subServiceId;
    private Integer mainServiceId;
    private ZonedDateTime createdAt;
}

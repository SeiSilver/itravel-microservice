package com.itravel.api.shop.payload;

import com.itravel.api.shop.enums.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopOnlyInfo {
    private Integer id;
    private String shopName;
    private Integer ownerId;
    private String ownerName;
    private String description;
    private Integer serviceCount;
    private ZonedDateTime createdAt;
    private String address;
    private ShopStatus status;
}

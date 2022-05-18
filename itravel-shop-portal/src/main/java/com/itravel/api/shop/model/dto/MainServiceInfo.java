package com.itravel.api.shop.model.dto;

import com.itravel.api.shop.enums.ServiceStatus;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MainServiceInfo {
    private Integer id;
    private String title;
    private Integer cityId;
    private String cityName;
    private Integer categoryId;
    private String categoryName;
    private String shopShopName;
    private Integer shopId;
    private List<String> images;
    private ServiceStatus status;
    private Float rateAverage;
    private Long rateCount;
    private String address;
    private Long lowestPrice;
    private ZonedDateTime createdAt;
}

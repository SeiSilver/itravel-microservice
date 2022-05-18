package com.itravel.api.shop.model.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.itravel.api.shop.enums.ServiceStatus;
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
public class FullServiceInfo {
  private Integer id;
  private String title;
  private String description;
  private String address;
  private Integer shopId;
  private String shopShopName;
  private Integer cityId;
  private String cityName;
  private Integer categoryId;
  private String categoryName;
  private ZonedDateTime eventStart;
  private ZonedDateTime eventEnd;
  private Integer duration;
  private Long rateCount;
  private Float rateAverage;
  private ServiceStatus status;
  private List<String> images;
  private List<SubServiceInfo> subServices;
}

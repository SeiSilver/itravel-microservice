package com.itravel.api.shop.model.dto;

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
public class SubServiceInfo {
  private Integer id;
  private String title;
  private Long price;
  private Integer stockAmount;
  private ServiceStatus status;
}

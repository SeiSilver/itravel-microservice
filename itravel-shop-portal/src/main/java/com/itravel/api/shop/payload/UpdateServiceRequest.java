package com.itravel.api.shop.payload;

import com.itravel.api.shop.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UpdateServiceRequest {
  @NotEmpty
  private String title;
  @NotEmpty
  private String description;
  @NotEmpty
  private String address;
  @NotNull
  private Integer cityId;
  @NotNull
  private Integer categoryId;
  @NotNull
  private Integer duration;
  @NotEmpty
  private String eventStart;
  @NotEmpty
  private String eventEnd;
  private ServiceStatus status;
  private List<String> images;
  @NotNull
  @Valid
  private List<UpdateSubServiceRequest> subServices;
}

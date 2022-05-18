package com.itravel.api.portal.auth.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BasicResponse {

  private Integer code;
  private String message;
  private String type;

}

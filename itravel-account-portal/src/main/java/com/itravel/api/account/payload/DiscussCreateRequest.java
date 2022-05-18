package com.itravel.api.account.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiscussCreateRequest {

  private String message;
  private Integer mainServiceId;
  private Integer discussParentId;

}

package com.itravel.api.payment.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PagingEntityDto {

  private int page;
  private long totalResults;
  private int totalPages;
  private List<Object> results;

}

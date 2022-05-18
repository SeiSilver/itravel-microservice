package com.itravel.api.shop.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
public class ArchiveDTO {

  @NotNull
  @NotBlank
  private String folderName;

  @NotNull
  private String fileContent;

}

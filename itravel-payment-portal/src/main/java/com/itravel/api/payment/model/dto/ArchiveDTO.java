package com.itravel.api.payment.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

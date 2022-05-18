package com.itravel.api.portal.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itravel.api.portal.auth.enums.AccountStatus;
import com.itravel.api.portal.auth.enums.GenderType;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

  private Long id;

  private String fullName;

  private String email;

  private String phoneNumber;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date birthday;

  private GenderType gender;

  private String imageLink;

  private AccountStatus status;

  private ZonedDateTime modifiedAt;

  private ZonedDateTime createdAt;

}

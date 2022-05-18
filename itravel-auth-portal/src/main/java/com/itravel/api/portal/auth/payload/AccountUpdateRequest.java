package com.itravel.api.portal.auth.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itravel.api.portal.auth.enums.GenderType;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountUpdateRequest {

  private String fullName;

  private String phoneNumber;

  @JsonFormat(locale = "yyyy-MM-dd")
  private Date birthday;

  private GenderType gender;

}

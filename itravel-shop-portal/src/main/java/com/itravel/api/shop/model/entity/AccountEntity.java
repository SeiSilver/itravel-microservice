package com.itravel.api.shop.model.entity;

import com.itravel.api.shop.enums.AccountStatus;
import com.itravel.api.shop.enums.GenderType;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

  private Long id;

  private String fullName;

  private String email;

  private String phoneNumber;

  private Date birthday;

  private GenderType gender;

  private List<RoleEntity> roles;

  private String imageLink;

  private AccountStatus status;

  private ZonedDateTime modifiedAt;

  private ZonedDateTime createdAt;

}

package com.itravel.api.account.model.entitydto;

import com.itravel.api.account.enums.RoleType;
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
public class RoleEntity {

  private Integer id;

  private RoleType roleName;

}

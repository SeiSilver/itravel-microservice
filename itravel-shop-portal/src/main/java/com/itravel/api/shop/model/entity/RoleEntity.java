package com.itravel.api.shop.model.entity;

import com.itravel.api.shop.enums.RoleType;
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

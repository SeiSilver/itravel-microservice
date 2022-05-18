package com.itravel.api.portal.auth.model.dto;

import com.itravel.api.portal.auth.enums.RoleType;
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
public class RoleInfo {

  private Integer id;

  private RoleType roleName;

}

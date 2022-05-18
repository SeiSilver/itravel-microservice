package com.itravel.api.portal.auth.payload;

import com.itravel.api.portal.auth.enums.RequestType;
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
public class AddSellerRoleRequestPayload {

  private RequestType requestType;

  private Long accountId;

  private RoleType role;

}

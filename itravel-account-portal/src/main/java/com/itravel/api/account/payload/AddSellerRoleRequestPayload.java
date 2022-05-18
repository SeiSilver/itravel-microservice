package com.itravel.api.account.payload;

import com.itravel.api.account.enums.RequestType;
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
public class AddSellerRoleRequestPayload {

  private RequestType requestType;

  private Long accountId;

  private RoleType role;

}

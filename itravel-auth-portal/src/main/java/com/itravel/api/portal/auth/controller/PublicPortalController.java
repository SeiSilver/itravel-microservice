package com.itravel.api.portal.auth.controller;

import com.itravel.api.portal.auth.controller.endpoint.PublicPortalEndPoint;
import com.itravel.api.portal.auth.model.dto.AccountPublicInfo;
import com.itravel.api.portal.auth.payload.GetAccountsByIdsPayload;
import com.itravel.api.portal.auth.service.AccountService;
import com.itravel.api.portal.auth.util.ModelMapperUtils;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(PublicPortalEndPoint.PUBLIC)
@RequiredArgsConstructor
@ApiIgnore
public class PublicPortalController {

  private final AccountService accountService;
  private final ModelMapperUtils modelMapperUtils;

  @PostMapping(PublicPortalEndPoint.PUBLIC_ACCOUNTS_INFO)
  @ApiOperation(value = "Get list of account with public info")
  public List<AccountPublicInfo> getAllAccountByIds(@RequestBody GetAccountsByIdsPayload getAccountsByIdsPayload) {
    return modelMapperUtils.mapList(accountService.getAllByAccountIds(getAccountsByIdsPayload.getListId()), AccountPublicInfo.class);
  }

}

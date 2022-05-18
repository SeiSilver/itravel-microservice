package com.itravel.api.account.controller;

import com.itravel.api.account.annotation.CurrentUser;
import com.itravel.api.account.constraint.CommonKey;
import com.itravel.api.account.controller.endpoint.AccountPortalEndPoint;
import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.exception.DiscussNotFoundException;
import com.itravel.api.account.model.dto.DiscussInfo;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.payload.DiscussCreateRequest;
import com.itravel.api.account.payload.DiscussUpdateRequest;
import com.itravel.api.account.security.principal.AccountPrincipal;
import com.itravel.api.account.service.DiscussService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AccountPortalEndPoint.DISCUSS)
@RequiredArgsConstructor
public class DiscussController {

  private final DiscussService discussService;

  @GetMapping(AccountPortalEndPoint.DISCUSS_BY_SERVICE_ID)
  @ApiOperation(value = "get all discuss at serviceId")
  public PagingEntityDto getAllDiscussByMainServiceId(
      @PathVariable Integer serviceId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size)
      throws AccountNotFoundException, DiscussNotFoundException {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return discussService.getDiscussByServiceId(paging, serviceId);
  }


  @PostMapping("")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "create discuss for serviceId")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public DiscussInfo createDiscuss(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestBody DiscussCreateRequest discussCreateRequest) {
    return discussService.createDiscuss(userPrincipal.getAccountVerified(), discussCreateRequest);
  }

  @PutMapping("")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "update discuss by discussId and serviceId")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void updateDiscuss(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @RequestBody DiscussUpdateRequest discussUpdateRequest) throws DiscussNotFoundException {
    discussService.updateDiscussByDiscussId(userPrincipal.getAccountVerified(), discussUpdateRequest);
  }

  @DeleteMapping(AccountPortalEndPoint.DISCUSS_DELETE_BY_ID)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "delete discuss by discuss id")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void deleteRateByMainServiceId(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Integer discussId) throws DiscussNotFoundException {
    discussService.deleteDiscussByDiscussId(userPrincipal.getAccountVerified(), discussId);
  }
}

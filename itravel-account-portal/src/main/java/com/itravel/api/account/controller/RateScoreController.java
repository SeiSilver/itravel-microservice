package com.itravel.api.account.controller;

import com.itravel.api.account.annotation.CurrentUser;
import com.itravel.api.account.constraint.CommonKey;
import com.itravel.api.account.controller.endpoint.AccountPortalEndPoint;
import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.exception.DataExistedException;
import com.itravel.api.account.exception.RateScoreNotFoundException;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.dto.RateScoreInfo;
import com.itravel.api.account.payload.RateInfoResponse;
import com.itravel.api.account.payload.RateScoreRequest;
import com.itravel.api.account.security.principal.AccountPrincipal;
import com.itravel.api.account.service.RateScoreService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(AccountPortalEndPoint.RATE)
@RequiredArgsConstructor
public class RateScoreController {

  private final RateScoreService rateScoreService;

  @GetMapping(AccountPortalEndPoint.RATE_BY_SERVICE_ID)
  @ApiOperation(value = "get all rate score at serviceId")
  public PagingEntityDto getAllRateByMainServiceId(
      @PathVariable Integer serviceId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size)
      throws RateScoreNotFoundException, AccountNotFoundException {
    if (page <= 1) {
      page = 0;
    } else {
      page--;
    }
    Pageable paging = PageRequest.of(page, size);
    return rateScoreService.getRateByServiceId(paging, serviceId);
  }

  @GetMapping(AccountPortalEndPoint.RATE_INFO_BY_SERVICE_ID)
  @ApiOperation(value = "get Rate averageScore and number of Rate at serviceId")
  public RateInfoResponse getRateInfoByMainServiceId(
      @PathVariable Integer serviceId)
      throws RateScoreNotFoundException {
    return rateScoreService.getRateInfoByServiceId(serviceId);
  }

  @PostMapping("")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "create a new rate score for serviceId")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public RateScoreInfo createRateByMainServiceId(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @Valid @RequestBody RateScoreRequest rateScoreRequest) throws DataExistedException {
    RateScoreInfo rateScoreInfo = rateScoreService.createRate(userPrincipal.getAccountVerified(), rateScoreRequest);
    rateScoreService.updateServiceRate(rateScoreRequest.getMainServiceId());
    return rateScoreInfo;
  }

  @DeleteMapping(AccountPortalEndPoint.RATE_BY_SERVICE_ID)
  @PreAuthorize("hasRole('USER')")
  @ApiOperation(value = "delete rate score by serviceId")
  @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
  public void deleteRateByMainServiceId(
      @ApiIgnore @CurrentUser AccountPrincipal userPrincipal,
      @PathVariable Integer serviceId) throws RateScoreNotFoundException {
    rateScoreService.deleteRateByServiceId(userPrincipal.getAccountVerified(), serviceId);
  }

}

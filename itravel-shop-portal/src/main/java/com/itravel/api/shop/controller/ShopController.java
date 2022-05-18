package com.itravel.api.shop.controller;

import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.config.ApplicationProperties;
import com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint;
import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.FullServiceInfo;
import com.itravel.api.shop.model.dto.ShopInfo;
import com.itravel.api.shop.payload.*;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.ITravelService;
import com.itravel.api.shop.service.ShopService;
import com.itravel.api.shop.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.time.format.DateTimeParseException;

import static com.itravel.api.shop.constraint.DefaultValue.PAGE;
import static com.itravel.api.shop.constraint.DefaultValue.SIZE;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.NAME_SHOP_NAME;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.OWNER;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SERVICE_SERVICE_ID;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SHOP_ID;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SHOP_ID_SERVICE;
import static com.itravel.api.shop.constraint.CommonKey.BEARER;

@RestController
@RequestMapping(ShopPortalEndpoint.SHOP)
@RequiredArgsConstructor
@Log4j2
public class ShopController {

  private final ShopService shopService;
  private final ITravelService iTravelService;
  private final RestTemplate restTemplate;
  private final ApplicationProperties properties;
  private final PayloadUtils payloadUtils;

  @GetMapping
  @PreAuthorize("hasRole('SELLER')")
  @ApiOperation(value = "Get current account's shop")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<ShopOnlyInfo> getOwnShop(@ApiIgnore @CurrentUser AccountPrincipal principal) throws ApplicationException {
    log.info("Receive get account {} shop:", principal.getAccountVerified().getId());
    ShopOnlyInfo shopOnlyInfo = shopService.findByOwnerId(Math.toIntExact(principal.getAccountVerified().getId()));
    return ResponseEntity.ok(shopOnlyInfo);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('USER','SELLER')")
  @ApiOperation(value = "Create new shop")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<?> createShop(@ApiIgnore @CurrentUser AccountPrincipal principal, @RequestBody CreateShopRequest request)
    throws ApplicationException{
    log.info("Receive create shop request with payload: {}", request);
    payloadUtils.validateRequiredFields(request);
    if(shopService.isOwnerOtherShop(Math.toIntExact(principal.getAccountVerified().getId())))
      throw new ApplicationException(ErrorCode.SHOP_EXIST, ErrorMessage.SHOP_EXIST);

    ResponseEntity<String> response = restTemplate.postForEntity(
        properties.getAuthPortal().getRegister(),
        principal.getAccountVerified().getId(),
        String.class
    );

    if (response.getStatusCode().compareTo(HttpStatus.OK) != 0)
      throw new ApplicationException(ErrorCode.REGISTER_FAIL, ErrorMessage.REGISTER_FAIL);


    ShopInfo shopInfo = shopService.create(principal.getAccountVerified(), principal.getUsername(), request);
    return ResponseEntity.ok(shopInfo);
  }

  @PostMapping(OWNER)
  @ApiOperation(value = "Check if account is shop owner")
  public ResponseEntity<OwnerResponse> checkOwner(
      @RequestBody OwnerRequest request
  ) throws ApplicationException {
    log.info("Receive shop owner check request for account {} and shop {}", request.getAccountId(), request.getObjectId());
    payloadUtils.validateRequiredFields(request);
    return ResponseEntity.ok(OwnerResponse.builder()
            .isOwner(shopService.isOwner(request.getAccountId(), request.getObjectId()))
        .build());
  }

  @PreAuthorize("hasRole('SELLER')")
  @PutMapping(SHOP_ID)
  @ApiOperation(value = "Update shop info")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<ShopInfo> update(
      @ApiIgnore @CurrentUser AccountPrincipal principal,
      @PathVariable Integer shopId, @RequestBody UpdateShopRequest request) throws ApplicationException {
    log.info("Receive update shop {} request with payload: {}", shopId, request);
    payloadUtils.validateRequiredFields(request);
    ShopInfo shopInfo = shopService.update(shopId,
            Math.toIntExact(principal.getAccountVerified().getId()),
        principal.getUsername(), request);
    return ResponseEntity.ok(shopInfo);
  }

  @PreAuthorize("hasRole('SELLER')")
  @DeleteMapping(SHOP_ID)
  @ApiOperation(value = "Delete shop")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<?> delete(
      @ApiIgnore @CurrentUser AccountPrincipal principal,
      @PathVariable Integer shopId) throws ApplicationException {
    log.info("Receive delete shop {} request", shopId);
    shopService.delete(shopId, Math.toIntExact(principal.getAccountVerified().getId()));
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('SELLER', 'MODERATOR')")
  @GetMapping(SHOP_ID_SERVICE)
  @ApiOperation(value = "Get shop service by service status. Default all")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<MainServiceInfoPageable> getShopService(
      @ApiIgnore @CurrentUser AccountPrincipal principal,
      @PathVariable Integer shopId,
      @RequestParam(name = "page", required = false, defaultValue = PAGE) Integer page,
      @RequestParam(name = "size", required = false, defaultValue = SIZE) Integer size
  ) throws ApplicationException {
    log.info("Receive get service by shop id: {}", shopId);
    MainServiceInfoPageable rs = shopService.findServiceInfoByShopId(shopId, page, size,
        principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR")),
        Math.toIntExact(principal.getAccountVerified().getId()));
    return ResponseEntity.ok(rs);
  }

  @PreAuthorize("hasRole('SELLER')")
  @PostMapping(SHOP_ID_SERVICE)
  @ApiOperation(value = "Create new shop service")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<?> createShopService(
      @ApiIgnore @CurrentUser AccountPrincipal principal,
      @PathVariable Integer shopId,
      @RequestBody CreateServiceRequest request) throws ApplicationException {
    log.info("Receive create service request: {}", request.toString());
    payloadUtils.validateRequiredFields(request);
    FullServiceInfo serviceInfo = iTravelService.create(shopId,
            Math.toIntExact(principal.getAccountVerified().getId()),
            request);
    return ResponseEntity.ok(serviceInfo);
  }

  @PreAuthorize("hasRole('SELLER')")
  @PutMapping(SERVICE_SERVICE_ID)
  @ApiOperation(value = "Update shop service by serviceId")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<FullServiceInfo> updateShopService(
      @ApiIgnore @CurrentUser AccountPrincipal principal,
      @PathVariable Integer serviceId,
      @RequestBody UpdateServiceRequest request) throws ApplicationException, DateTimeParseException {
    log.info("Receive update shopService request: {}", request.toString());
    payloadUtils.validateRequiredFields(request);
    FullServiceInfo serviceInfo = iTravelService.update(serviceId, Math.toIntExact(principal.getAccountVerified().getId()), request);
    return ResponseEntity.ok(serviceInfo);
  }

  @PreAuthorize("hasRole('SELLER')")
  @DeleteMapping(SERVICE_SERVICE_ID)
  @ApiOperation(value = "Delete shop service")
  @Operation(security = @SecurityRequirement(name = BEARER))
  public ResponseEntity<?> deleteShopService(
      @ApiIgnore @CurrentUser AccountPrincipal principal,
      @PathVariable Integer serviceId) throws ApplicationException {
    log.info("Receive delete shop service with serviceId: {}", serviceId);
    iTravelService.delete(serviceId, Math.toIntExact(principal.getAccountVerified().getId()));
    return ResponseEntity.ok().build();
  }

    @GetMapping(SHOP_ID)
    @ApiOperation(value = "Find shop by shopId. page and size for control the number of shop service will be display")
    public ResponseEntity<ShopOnlyInfo> findByShopId(@PathVariable Integer shopId
    ) throws ApplicationException {
        log.info("Receive find shop only info request with id: {}", shopId);
        return ResponseEntity.ok(shopService.findShopOnlyInfo(shopId));
    }

    @GetMapping(NAME_SHOP_NAME)
  @ApiOperation(value = "Find shop by name. Pagination support")
  public ResponseEntity<PaginationShopInfo> findByShopName(
      @PathVariable String shopName,
      @RequestParam(name = "page", required = false, defaultValue = PAGE) Integer page,
      @RequestParam(name = "size", required = false, defaultValue = SIZE) Integer size
    ){
      log.info("Receive find shop only info request with name: {}", shopName);

      return ResponseEntity.ok(shopService.findShopOnlyInfoByShopName(shopName, page, size));
    }
}

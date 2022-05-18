package com.itravel.api.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint;
import com.itravel.api.shop.enums.ServiceStatus;
import com.itravel.api.shop.enums.ShopStatus;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.exception.MissingRequireFieldException;
import com.itravel.api.shop.payload.FindServiceByListStatusRequest;
import com.itravel.api.shop.payload.GetShopByStatusesRequest;
import com.itravel.api.shop.payload.PaginationServiceInfo;
import com.itravel.api.shop.payload.PaginationShopInfo;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.ITravelService;
import com.itravel.api.shop.service.ShopService;
import com.itravel.api.shop.util.PayloadUtils;
import com.itravel.api.shop.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;

import static com.itravel.api.shop.constraint.CommonKey.BEARER;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SERVICE;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SERVICE_SERVICE_ID;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SHOP;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SHOP_SHOP_ID;
import static com.itravel.api.shop.constraint.DefaultValue.SIZE;
import static com.itravel.api.shop.constraint.DefaultValue.PAGE;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SHOP_STATUS;

@RequiredArgsConstructor
@RequestMapping(ShopPortalEndpoint.MOD)
@RestController
@Log4j2
public class ModController {
    private final ShopService shopService;
    private final ITravelService iTravelService;
    private final PayloadUtils payloadUtils;

    private final Set<ServiceStatus> modUpdatableStatus =Set.of(ServiceStatus.ACTIVE, ServiceStatus.BLOCKED);

    @GetMapping(SHOP)
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @ApiOperation(value = "Get all shop")
    @Operation(security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<PaginationShopInfo> getListShop(
        @RequestParam(name = "page", required = false, defaultValue = PAGE) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = SIZE) Integer size
    ){
        log.info("Receive get shop list at page: {} with size: {}",page, size);
        PaginationShopInfo rs = shopService.findAllShop(page, size);
        return ResponseEntity.ok(rs);
    }

    @PutMapping(SHOP_SHOP_ID)
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @ApiOperation(value = "Update shop status")
    @Operation(security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<String> updateShopStatus(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @PathVariable Integer shopId,
        @RequestParam ShopStatus status
        ) throws ApplicationException {
        log.info("Receive update date shop {} to status: {}", shopId, status.name());
        shopService.updateStatus(shopId, status, Math.toIntExact(principal.getAccountVerified().getId()));
        return ResponseEntity.ok("Success");
    }

    @PostMapping(SHOP_STATUS)
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @ApiOperation(value = "Get shop by status")
    @Operation(security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<PaginationShopInfo> findByStatus(
        @RequestBody GetShopByStatusesRequest request,
        @RequestParam(name = "page", required = false, defaultValue = PAGE) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = SIZE) Integer size
    ) throws JsonProcessingException, MissingRequireFieldException {
        log.info("Receive find shop only info request with status: {}", StringUtils.toJson(request.getStatuses()));
        payloadUtils.validateRequiredFields(request);
        return ResponseEntity.ok(shopService.findShopOnlyInfoByStatus(request.getStatuses(), page, size));
    }

    @PostMapping(SERVICE)
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @ApiOperation(value = "Get service by statuses. Pagination support")
    @Operation(security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<PaginationServiceInfo> findByStatuses(
        @RequestBody FindServiceByListStatusRequest request,
        @RequestParam(name = "page", required = false, defaultValue = PAGE) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = SIZE) Integer size
    ) throws ApplicationException {
        log.info("Receive find service info request with status: {}", request.getStatuses().toString());
        payloadUtils.validateRequiredFields(request);
        return ResponseEntity.ok(iTravelService.findByShopIdAndListStatus(request.getShopId(), request.getStatuses(), page, size));
    }

    @PutMapping(SERVICE_SERVICE_ID)
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @ApiOperation(value = "Update service status. Only to ACTIVE or BLOCKED")
    @Operation(security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<String> updateServiceStatus(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @PathVariable Integer serviceId,
        @RequestParam ServiceStatus status
    ) throws ApplicationException{
        if(modUpdatableStatus.contains(status)) {
            iTravelService.updateStatus(serviceId, status, Math.toIntExact(principal.getAccountVerified().getId()));
            return ResponseEntity.ok("Success");
        }else
            return ResponseEntity.status(200).body("Moderator/Admin do not have permission update service to " + status.name());
    }
}

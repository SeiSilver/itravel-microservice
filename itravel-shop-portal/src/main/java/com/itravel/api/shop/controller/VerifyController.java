package com.itravel.api.shop.controller;

import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.constraint.DefaultValue;
import com.itravel.api.shop.enums.ServiceStatus;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.payload.ListVerifyResponse;
import com.itravel.api.shop.payload.UpdateVerifyStatusRequest;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.ITravelService;
import com.itravel.api.shop.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;

import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.VERIFY;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SERVICE_SERVICE_ID;

@RequiredArgsConstructor
@RequestMapping(VERIFY)
@RestController
@Log4j2
public class VerifyController {
    private final ITravelService iTravelService;
    private final PayloadUtils payloadUtils;
    private final Set<ServiceStatus> verifiableStatusSet = Set.of(ServiceStatus.ACTIVE, ServiceStatus.REJECT);

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR')")
    @ApiOperation(value = "Get list service verify request. Pagination support.")
    public ResponseEntity<ListVerifyResponse> getVerifyList(
        @RequestParam(name = "page", required = false, defaultValue = DefaultValue.PAGE) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = DefaultValue.SIZE) Integer size
    ){
        log.info("Receive get list verify request for service");
        return ResponseEntity.ok(iTravelService.getListVerifyResponse(page, size));
    }

    @PutMapping(SERVICE_SERVICE_ID)
    @PreAuthorize("hasRole('MODERATOR')")
    @ApiOperation(value = "Update service: ACTIVE or REJECT")
    public ResponseEntity<String> updateStatus(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @RequestBody UpdateVerifyStatusRequest request) throws ApplicationException {
        payloadUtils.validateRequiredFields(request);
        if(verifiableStatusSet.contains(request.getStatus())) {
            iTravelService.verify(request, Math.toIntExact(principal.getAccountVerified().getId()));
            return ResponseEntity.ok("Success");
        }else
            return ResponseEntity.status(200).body("Moderator/Admin do not have permission update service to " + request.getStatus().name());
    }
}

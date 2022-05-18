package com.itravel.api.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.constraint.CommonKey;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.payload.CreateOrderRequest;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.OrderService;
import com.itravel.api.shop.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.ORDER;

@RequiredArgsConstructor
@RequestMapping(ORDER)
@RestController
@Log4j2
public class OrderController {
    private final OrderService orderService;
    private final PayloadUtils payloadUtils;

    @PostMapping
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    @ApiOperation(value = "Request to create order")
    public ResponseEntity<String> createOrder(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @RequestBody CreateOrderRequest request
        ) throws ApplicationException, JsonProcessingException {
        log.info("Receive create order request with payload: {}", request);
        payloadUtils.validateRequiredFields(request);
        String rs =orderService.createOrder(
            Math.toIntExact(principal.getAccountVerified().getId()),
            request
        );
        return ResponseEntity.ok(rs);
    }
}

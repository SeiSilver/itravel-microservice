package com.itravel.api.shop.controller;

import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.constraint.CommonKey;
import com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.CartItemInfo;
import com.itravel.api.shop.payload.*;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.CartService;
import com.itravel.api.shop.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(ShopPortalEndpoint.CART)
@RequiredArgsConstructor
@Log4j2
public class CartController {
    private final CartService cartService;
    private final PayloadUtils payloadUtils;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Add service to cart")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<String> addCartItem(
            @ApiIgnore @CurrentUser AccountPrincipal principal,
            @RequestBody CartRequest request) throws ApplicationException {
        payloadUtils.validateRequiredFields(request);
        cartService.add(
                Math.toIntExact(principal.getAccountVerified().getId()),
                request
        );
        return ResponseEntity.ok("Success");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Find all cart item by current account id")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<List<CartItemInfo>> findAll(
            @ApiIgnore @CurrentUser AccountPrincipal principal) throws ApplicationException {
        List<CartItemInfo> cartItemInfos = cartService.findByAccountId(
                Math.toIntExact(principal.getAccountVerified().getId()));
        return ResponseEntity.ok(cartItemInfos);
    }

    @GetMapping(ShopPortalEndpoint.CART_ITEM_ID)
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Find cart item by id")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<CartItemInfo> findById(
            @ApiIgnore @CurrentUser AccountPrincipal principal,
            @PathVariable Integer cartItemId
    ) throws ApplicationException {
        CartItemInfo cartItemInfo = cartService.findById(
                cartItemId,
                Math.toIntExact(principal.getAccountVerified().getId())
        );
        return ResponseEntity.ok(cartItemInfo);
    }

    @PutMapping(ShopPortalEndpoint.CART_ITEM_ID)
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Update cart item")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<CartItemInfo> update(
            @ApiIgnore @CurrentUser AccountPrincipal principal,
            @PathVariable Integer cartItemId,
            @RequestBody UpdateCartItemRequest request
            ) throws ApplicationException {
        log.info("Receive update cart item request with payload: {}", request);
        payloadUtils.validateRequiredFields(request);
        CartItemInfo cartItemInfo = cartService.update(
                Math.toIntExact(principal.getAccountVerified().getId()),
                cartItemId,
                request
        );
        return ResponseEntity.ok(cartItemInfo);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Delete cart item")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<?> delete(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @RequestBody DeleteCartRequest request
        ) throws ApplicationException{
        payloadUtils.validateRequiredFields(request);
        cartService.delete(
                Math.toIntExact(principal.getAccountVerified().getId()),
                request.getCartIds()
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiOperation(value = "Update list cart item")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<ListCartItemResponse> updateList(
        @RequestBody UpdateListCartItemRequest request
        ) throws ApplicationException {
        log.info("Receive update list cart item request with payload: {}", request);
        payloadUtils.validateRequiredFields(request);
        List<CartItemInfo> infos = cartService.updateListCart(request);
        ListCartItemResponse rs = ListCartItemResponse.builder().cartItemInfos(infos).build();
        return ResponseEntity.ok(rs);
    }
}

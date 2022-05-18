package com.itravel.api.payment.controller;

import com.itravel.api.payment.annotation.CurrentUser;
import com.itravel.api.payment.config.ApplicationProperties;
import com.itravel.api.payment.constraint.CommonKey;
import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;
import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.dto.OrderDetail;
import com.itravel.api.payment.model.dto.OrderItemInfo;
import com.itravel.api.payment.model.dto.OrderSummary;
import com.itravel.api.payment.model.entity.OrderEntity;
import com.itravel.api.payment.model.entity.OrderItemEntity;
import com.itravel.api.payment.model.entitydto.RoleEntity;
import com.itravel.api.payment.payload.ListOrderStatus;
import com.itravel.api.payment.payload.OwnerRequest;
import com.itravel.api.payment.payload.OwnerResponse;
import com.itravel.api.payment.payload.PaginationOrderItemInfo;
import com.itravel.api.payment.payload.PaginationOrderSummary;
import com.itravel.api.payment.payload.SellerOrder;
import com.itravel.api.payment.payload.ServiceFrequencyResponse;
import com.itravel.api.payment.payload.TotalOrderItemPrice;
import com.itravel.api.payment.security.principal.AccountPrincipal;
import com.itravel.api.payment.service.OrderService;
import com.itravel.api.payment.util.ModelMapperUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.FREQUENCY;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.ORDER;
import static com.itravel.api.payment.constraint.DefaultValue.SIZE;
import static com.itravel.api.payment.constraint.DefaultValue.PAGE;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.ORDER_ID;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.SHOP_SHOP_ID;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.SHOP_SHOP_ID_ORDER_ID;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.STATUS;

@RequiredArgsConstructor
@RequestMapping(ORDER)
@RestController
@Log4j2
public class OrderController {
    private final OrderService orderService;
    private final ModelMapperUtils mapperUtils;
    private final RestTemplate restTemplate;
    private final ApplicationProperties properties;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Get all current account's order. Pagination support")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<PaginationOrderSummary> getCurrentAccountAllOrder(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size
        ){
        log.info("Receive get account {} order", principal.getAccountVerified().getId());
        Page<OrderEntity> pages = orderService.findByAccountId(
            principal.getAccountVerified().getId(),
            page, size);
        List<OrderSummary> orderSummaries = toOrderSummary(pages.getContent());

        return ResponseEntity.ok(
            PaginationOrderSummary.builder()
                .page(page)
                .size(size)
                .totalPage(pages.getTotalPages())
                .totalItem(pages.getTotalElements())
                .results(orderSummaries)
                .build()
        );
    }

    @GetMapping(ORDER_ID)
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Get order detail")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<OrderDetail> getOrderDetail(
        @ApiIgnore @CurrentUser AccountPrincipal principal,
        @PathVariable Integer orderId
    ) throws ApplicationException {
        OrderEntity orderEntity = orderService.findById(orderId);
        Integer accountId = Math.toIntExact(principal.getAccountVerified().getId());
        if(!orderService.isOrderOwner(accountId, Math.toIntExact(orderEntity.getAccountId()))){
            throw new ApplicationException(ErrorCode.NOT_ORDER_OWNER,
                ErrorMessage.NOT_ORDER_OWNER);
        }
        OrderDetail orderDetail = mapperUtils.map(orderEntity, OrderDetail.class);
        orderDetail.setOrderItemInfos(mapperUtils.mapList(orderEntity.getOrderItems(), OrderItemInfo.class));
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping(FREQUENCY)
    @ApiOperation(value = "Get the most popular service")
    public ResponseEntity<ServiceFrequencyResponse> getServiceFrequency(){
        log.info("Receive get service frequency");
        return ResponseEntity.ok(
            ServiceFrequencyResponse.builder()
                .serviceFrequencies(orderService.getServiceFrequency())
                .build()
        );
    }

    @PostMapping(STATUS)
    @ApiOperation(value = "Find order by status. Pagination support")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<PaginationOrderSummary> findByStatuses(
        @RequestBody ListOrderStatus request,
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size
    ){
        log.info("Receive get order by statuses");
        Page<OrderEntity> pages = orderService.findAllByStatusIn(page, size, request.getStatuses());
        List<OrderSummary> orderSummaries = toOrderSummary(pages.getContent());

        return ResponseEntity.ok(
            PaginationOrderSummary.builder()
                .page(page)
                .size(size)
                .totalPage(pages.getTotalPages())
                .totalItem(pages.getTotalElements())
                .results(orderSummaries)
                .build()
        );
    }

    @GetMapping(SHOP_SHOP_ID)
    @ApiOperation(value = "Get all order by shopId. Pagination support.")
    @PreAuthorize("hasAnyRole('SELLER', 'MODERATOR', 'ADMIN')")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<PaginationOrderSummary> getOrderByShopId(
        @PathVariable Integer shopId,
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size,
        @ApiIgnore @CurrentUser AccountPrincipal principal
    ) throws ApplicationException {
        log.info("Receive get all shop order");

        if(!(hasRole("MODERATOR", principal.getAccountVerified().getRoles())
        && hasRole("ADMIN", principal.getAccountVerified().getRoles()))) {

            OwnerResponse response = restTemplate.postForObject(properties.getShopPortal().getShopOwner(),
                OwnerRequest.builder().accountId(Math.toIntExact(principal.getAccountVerified().getId())).objectId(shopId).build(),
                OwnerResponse.class
            );
            if (!response.getIsOwner())
                throw new ApplicationException(
                    ErrorCode.NOT_SHOP_OWNER, ErrorMessage.NOT_SHOP_OWNER
                );
        }
        Page<SellerOrder> pages = orderService.findSellerOrderByShopId(shopId, page, size);

        List<OrderSummary> orderSummaries = new ArrayList<>();
        for(SellerOrder sellerOrder : pages.getContent()){
            OrderSummary orderSummary = mapperUtils.map(sellerOrder, OrderSummary.class);
            Integer totalPrice = 0;
           List<TotalOrderItemPrice> totalOrderItemPrices = orderService.getTotalPriceByOrderIdAndShopId(orderSummary.getId(), shopId);
           for(TotalOrderItemPrice totalOrderItemPrice : totalOrderItemPrices)
               totalPrice+= totalOrderItemPrice.getTotalPrice() == null ? 0 : totalOrderItemPrice.getTotalPrice();
            orderSummary.setTotalPrice(totalPrice);
            orderSummaries.add(orderSummary);
        }

        return ResponseEntity.ok(
            PaginationOrderSummary.builder()
                .page(page)
                .size(size)
                .totalPage(pages.getTotalPages())
                .totalItem(pages.getTotalElements())
                .results(orderSummaries)
                .build()
        );
    }

    @GetMapping(SHOP_SHOP_ID_ORDER_ID)
    @ApiOperation(value = "Find order item by shop and order. Pagination support")
    @PreAuthorize("hasAnyRole('SELLER', 'MODERATOR', 'ADMIN')")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<PaginationOrderItemInfo> findByShopAndOrder(
        @PathVariable Integer shopId, @PathVariable Integer orderId,
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size,
        @ApiIgnore @CurrentUser AccountPrincipal principal
    ) throws ApplicationException {
        log.info("Receive get order item info by shop {} and order {}", shopId, orderId);

        if(!(hasRole("ROLE_MODERATOR", principal.getAccountVerified().getRoles())
            && hasRole("ROLE_ADMIN", principal.getAccountVerified().getRoles()))) {

            OwnerResponse response = restTemplate.postForObject(properties.getShopPortal().getShopOwner(),
                OwnerRequest.builder().accountId(Math.toIntExact(principal.getAccountVerified().getId())).objectId(shopId).build(),
                OwnerResponse.class
            );
            if (!response.getIsOwner())
                throw new ApplicationException(
                    ErrorCode.NOT_SHOP_OWNER, ErrorMessage.NOT_SHOP_OWNER
                );
        }

        Page<OrderItemEntity> pages = orderService.findByShopIdAndOrderId(shopId, orderId, page, size);

        Integer totalPrice = 0;
        for(OrderItemEntity orderItemEntity: pages.getContent()){
            totalPrice += (orderItemEntity.getPrice() * orderItemEntity.getQuantity());
        }
        OrderEntity orderEntity = pages.getContent().get(0).getOrder();

        return ResponseEntity.ok(
            PaginationOrderItemInfo.builder()
                .page(page)
                .size(size)
                .fullName(orderEntity.getFullName())
                .phoneNumber(orderEntity.getPhoneNumber())
                .status(orderEntity.getStatus())
                .createdAt(orderEntity.getCreatedAt())
                .totalPrice(totalPrice)
                .totalItem(pages.getTotalElements())
                .totalPage(pages.getTotalPages())
                .results(mapperUtils.mapList(pages.getContent(), OrderItemInfo.class))
                .build()
        );
    }


    private List<OrderSummary> toOrderSummary(List<OrderEntity> entities){
        List<OrderSummary> orderSummaries = new ArrayList<>();
        for(OrderEntity orderEntity :entities){
            OrderSummary orderSummary = mapperUtils.map(orderEntity, OrderSummary.class);
            List<OrderItemInfo> orderItemSummaries = mapperUtils.mapList(orderEntity.getOrderItems(), OrderItemInfo.class);
            orderSummary.setOrderItemInfos(orderItemSummaries);
            orderSummaries.add(orderSummary);
        }
        return orderSummaries;
    }

    boolean hasRole(String name, List<RoleEntity> roleEntities){
        for (RoleEntity roleEntity : roleEntities)
            if(roleEntity.getRoleName().name().equalsIgnoreCase(name))
                return true;
        return false;
    }
}

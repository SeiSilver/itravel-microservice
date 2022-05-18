package com.itravel.api.payment.controller;

import com.itravel.api.payment.constraint.CommonKey;
import com.itravel.api.payment.model.dto.OrderItemInfo;
import com.itravel.api.payment.model.dto.OrderSummary;
import com.itravel.api.payment.model.entity.OrderEntity;
import com.itravel.api.payment.payload.PaginationOrderSummary;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.itravel.api.payment.constraint.DefaultValue.PAGE;
import static com.itravel.api.payment.constraint.DefaultValue.SIZE;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.MOD;
import static com.itravel.api.payment.controller.endpoint.AccountPortalEndPoint.ORDER_ACCOUNT_ACCOUNT_ID;

@RequiredArgsConstructor
@RequestMapping(MOD)
@RestController
@Log4j2
public class ModController {
    private final OrderService orderService;
    private final ModelMapperUtils mapperUtils;

    @GetMapping(ORDER_ACCOUNT_ACCOUNT_ID)
    @PreAuthorize("hasRole('MODERATOR')")
    @ApiOperation(value = "Get all order by accountId. Pagination support")
    @Operation(security = @SecurityRequirement(name = CommonKey.BEARER))
    public ResponseEntity<PaginationOrderSummary> getCurrentAccountAllOrder(
        @PathVariable Integer accountId,
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size
    ){
        log.info("Receive get account {} order", accountId);
        Page<OrderEntity> pages = orderService.findByAccountId(
            Long.valueOf(accountId),
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
}

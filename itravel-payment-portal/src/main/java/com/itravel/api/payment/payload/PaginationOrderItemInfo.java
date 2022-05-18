package com.itravel.api.payment.payload;

import com.itravel.api.payment.enums.OrderStatus;
import com.itravel.api.payment.model.dto.OrderItemInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaginationOrderItemInfo {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalItem;
    private String fullName;
    private String phoneNumber;
    private Integer totalPrice;
    private OrderStatus status;
    private ZonedDateTime createdAt;
    private List<OrderItemInfo> results;
}

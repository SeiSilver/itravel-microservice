package com.itravel.api.payment.model.dto;

import com.itravel.api.payment.enums.OrderStatus;
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
public class OrderSummary {
    private Integer id;
    private String orderBillId;
    private Integer totalPrice;
    private OrderStatus status;
    private List<OrderItemInfo> orderItemInfos;
    private ZonedDateTime createdAt;
    private String fullName;
    private String phoneNumber;
}

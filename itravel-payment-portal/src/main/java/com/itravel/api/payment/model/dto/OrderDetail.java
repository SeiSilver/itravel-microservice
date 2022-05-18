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
public class OrderDetail {
    private Integer id;
    private String orderBillId;
    private String fullName;
    private String phoneNumber;
    private Integer totalPrice;
    private OrderStatus status;
    private ZonedDateTime createdAt;
    private List<OrderItemInfo> orderItemInfos;
}

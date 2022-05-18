package com.itravel.api.payment.payload;

import com.itravel.api.payment.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
public class SellerOrder {
    private Integer id;
    private String orderBillId;
    private String fullName;
    private String phoneNumber;
    private Integer totalPrice;
    private OrderStatus status;
    private ZonedDateTime createdAt;

    public SellerOrder(Integer id, String orderBillId, String fullName, String phoneNumber, OrderStatus status, ZonedDateTime createdAt) {
        this.id = id;
        this.orderBillId = orderBillId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
    }
}

package com.itravel.api.payment.payload;

import com.itravel.api.payment.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOrderStatus {
    private List<OrderStatus> statuses;
}

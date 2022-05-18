package com.itravel.api.payment.payload;

import com.itravel.api.payment.model.dto.OrderInfo;
import com.itravel.api.payment.model.dto.OrderItemInfo;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrderRequest {

  private OrderInfo order;

  private List<OrderItemInfo> orderItems;

}

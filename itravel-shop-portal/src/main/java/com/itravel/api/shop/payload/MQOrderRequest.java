package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.dto.OrderInfo;
import com.itravel.api.shop.model.dto.OrderItemInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MQOrderRequest {

  private OrderInfo order;

  private List<OrderItemInfo> orderItems;

}

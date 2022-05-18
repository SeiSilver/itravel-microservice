package com.itravel.api.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.CartItemInfo;
import com.itravel.api.shop.payload.CreateOrderRequest;
import com.itravel.api.shop.payload.MQOrderRequest;

import java.time.ZonedDateTime;
import java.util.List;

public interface OrderService {
    String createOrder(Integer accountId, CreateOrderRequest request) throws ApplicationException, JsonProcessingException;
    Boolean isValidUsedDate(ZonedDateTime usedStart, ZonedDateTime eventStart, ZonedDateTime eventEnd);
}

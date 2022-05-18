package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
public class CartItemRequest {
    @NotNull
    private Integer subServiceId;
    @NotNull
    @Positive
    private Integer quantity;
}

package com.itravel.api.shop.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
public class CartItemsRequest {
    @NotNull
    private Integer cartItemId;
    @Positive
    private Integer quantity;
    @NotNull
    private String usedStart;
}

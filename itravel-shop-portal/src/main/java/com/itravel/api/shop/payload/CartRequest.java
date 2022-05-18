package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
public class CartRequest {
    @NotEmpty
    private String dateStart;
    @Valid
    @NotNull
    private List<CartItemRequest> cartItems;
}

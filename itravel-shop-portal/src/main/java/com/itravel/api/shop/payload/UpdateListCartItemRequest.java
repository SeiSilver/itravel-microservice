package com.itravel.api.shop.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class UpdateListCartItemRequest {
    @NotNull
    @Valid
    private List<CartItemsRequest> requests;
}

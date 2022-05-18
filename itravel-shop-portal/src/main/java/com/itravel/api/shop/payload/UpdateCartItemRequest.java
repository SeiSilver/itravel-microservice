package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class UpdateCartItemRequest {
    @NotNull
    @Positive
    private Integer quantity;
    @NotEmpty
    private String usedStart;
}

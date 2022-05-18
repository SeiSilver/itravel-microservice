package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
public class CreateSubServiceRequest {
    @NotEmpty
    private String title;
    @NotNull
    private Long price;
    @NotNull
    private Integer stockAmount;
}

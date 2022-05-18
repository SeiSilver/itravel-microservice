package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UpdateSubServiceRequest {
    @NotNull
    private Integer id;
    @NotEmpty
    private String title;
    @NotNull
    private Long price;
    @NotNull
    private Integer stockAmount;
    @NotEmpty
    private String action;
}

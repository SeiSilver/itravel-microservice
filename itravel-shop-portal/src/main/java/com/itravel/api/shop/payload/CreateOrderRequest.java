package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreateOrderRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String phoneNumber;
    @NotEmpty
    private List<Integer> cartIds;
}

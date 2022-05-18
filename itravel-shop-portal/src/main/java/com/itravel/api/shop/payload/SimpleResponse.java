package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SimpleResponse {
    private Boolean success;
}

package com.itravel.api.shop.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateRateRequest {
    private Integer mainServiceId;
    private Long rateCount;
    private Float rateAverage;
}

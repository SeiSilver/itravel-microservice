package com.itravel.api.account.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateRateRequest {
    private Integer mainServiceId;
    private Long rateCount;
    private Float rateAverage;
}

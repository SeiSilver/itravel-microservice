package com.itravel.api.shop.payload;

import com.itravel.api.shop.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateVerifyStatusRequest {
    @NotNull
    private Integer serviceId;
    @NotNull
    private ServiceStatus status;
    @NotEmpty
    private String refuseReason;
}

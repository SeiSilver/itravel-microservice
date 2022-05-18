package com.itravel.api.shop.payload;

import com.itravel.api.shop.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FindServiceByListStatusRequest {
    @NotNull
    private List<ServiceStatus> statuses;
    @NotNull
    private Integer shopId;
}

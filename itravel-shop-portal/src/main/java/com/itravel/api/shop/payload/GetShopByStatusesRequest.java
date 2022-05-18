package com.itravel.api.shop.payload;

import com.itravel.api.shop.enums.ShopStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetShopByStatusesRequest {
    @NotNull
    List<ShopStatus> statuses;
}

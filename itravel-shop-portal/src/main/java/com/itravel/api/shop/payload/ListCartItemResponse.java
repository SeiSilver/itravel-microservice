package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.dto.CartItemInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ListCartItemResponse {
    List<CartItemInfo> cartItemInfos;
}

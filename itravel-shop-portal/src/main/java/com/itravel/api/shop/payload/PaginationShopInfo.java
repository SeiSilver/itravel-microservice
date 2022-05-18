package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaginationShopInfo {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalItem;
    private List<ShopOnlyInfo> shopInfos;
}

package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.dto.MainServiceInfo;
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
public class PaginationServiceInfo {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalItem;
    private List<MainServiceInfo> serviceInfos;
}

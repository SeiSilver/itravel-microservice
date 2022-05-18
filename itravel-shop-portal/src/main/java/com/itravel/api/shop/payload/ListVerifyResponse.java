package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.dto.FullServiceInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ListVerifyResponse {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalElement;
    private List<FullServiceInfo> services;
}

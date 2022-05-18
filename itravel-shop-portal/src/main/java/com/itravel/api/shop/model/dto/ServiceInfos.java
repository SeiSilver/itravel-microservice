package com.itravel.api.shop.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServiceInfos {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalItem;
    List<FullServiceInfo> serviceInfos;
}

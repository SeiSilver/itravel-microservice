package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.dto.MainServiceInfo;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MainServiceInfoPageable {
    private Integer page;
    private List<MainServiceInfo> results;
    private Integer totalPages;
    private Integer totalResults;
}

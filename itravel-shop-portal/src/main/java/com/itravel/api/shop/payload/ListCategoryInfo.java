package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.dto.CategoryInfo;
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
public class ListCategoryInfo {
    private List<CategoryInfo> categoryInfos;
}

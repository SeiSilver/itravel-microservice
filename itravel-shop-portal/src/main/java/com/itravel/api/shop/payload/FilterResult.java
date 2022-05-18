package com.itravel.api.shop.payload;

import com.itravel.api.shop.model.entity.MainServiceEntity;
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
public class FilterResult {
    private List<MainServiceEntity> mainServiceEntities;
//    private Long
}

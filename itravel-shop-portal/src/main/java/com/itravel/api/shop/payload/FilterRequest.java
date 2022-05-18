package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterRequest {
    private String title;
    private List<Integer> categoryIds;
    private Integer sortType;
    private Integer page;
    private Integer size;
}

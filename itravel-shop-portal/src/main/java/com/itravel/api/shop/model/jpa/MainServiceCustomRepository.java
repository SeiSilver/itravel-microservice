package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.payload.FilterRequest;

import java.util.List;

public interface MainServiceCustomRepository {
    List<MainServiceEntity> filter(FilterRequest request);
}

package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.CityEntity;

public interface CityService {
  CityEntity findById(Integer id) throws ApplicationException;
}

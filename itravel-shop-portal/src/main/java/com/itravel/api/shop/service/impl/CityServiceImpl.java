package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.CityEntity;
import com.itravel.api.shop.model.jpa.CityRepository;
import com.itravel.api.shop.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
  private final CityRepository cityRepository;

  @Override
  public CityEntity findById(Integer id) throws ApplicationException {
    return cityRepository.findById(id).orElseThrow(
        () -> new ApplicationException(ErrorCode.CITY_NOT_FOUND, ErrorMessage.CITY_NOT_FOUND)
    );
  }
}

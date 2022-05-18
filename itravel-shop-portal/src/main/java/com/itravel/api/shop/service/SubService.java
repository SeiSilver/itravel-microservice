package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import org.springframework.stereotype.Service;

@Service
public interface SubService {
    SubServiceEntity findSubById(Integer id) throws ApplicationException;
}

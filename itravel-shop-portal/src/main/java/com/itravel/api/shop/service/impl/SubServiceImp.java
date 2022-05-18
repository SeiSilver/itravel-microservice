package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import com.itravel.api.shop.model.jpa.SubServiceRepository;
import com.itravel.api.shop.service.SubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubServiceImp implements SubService {
    private final SubServiceRepository subRepository;

    @Override
    public SubServiceEntity findSubById(Integer id) throws ApplicationException {
        return subRepository.findById(id).orElseThrow(
            () -> new ApplicationException(
                ErrorCode.SUB_SERVICE_NOT_FOUND,
                ErrorMessage.SUB_SERVICE_NOT_FOUND
            )
        );
    }
}

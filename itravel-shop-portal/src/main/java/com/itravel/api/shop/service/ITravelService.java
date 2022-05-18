package com.itravel.api.shop.service;

import com.itravel.api.shop.enums.ServiceStatus;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.FullServiceInfo;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import com.itravel.api.shop.payload.CreateServiceRequest;
import com.itravel.api.shop.payload.FilterRequest;
import com.itravel.api.shop.payload.ListVerifyResponse;
import com.itravel.api.shop.payload.PaginationServiceInfo;
import com.itravel.api.shop.payload.UpdateRateRequest;
import com.itravel.api.shop.payload.UpdateServiceRequest;
import com.itravel.api.shop.payload.UpdateVerifyStatusRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ITravelService {
    Optional<MainServiceEntity> findOptionalById(Integer id);
    MainServiceEntity findEntityById(Integer id) throws ApplicationException;
    PaginationServiceInfo findAll(Integer page, Integer size, List<ServiceStatus> statuses) throws ApplicationException;
    PaginationServiceInfo findByShopIdAndListStatus(Integer shopId, List<ServiceStatus> statuses, Integer page, Integer size) throws ApplicationException;
    Page<MainServiceEntity> findEntityByShopId(Integer id, Integer page, Integer size);
    SubServiceEntity findSubById(Integer id) throws ApplicationException;
    FullServiceInfo findServiceInfoById(Integer accountId, Integer id, List<ServiceStatus> statuses) throws ApplicationException;
    FullServiceInfo create(Integer shopId, Integer accountId, CreateServiceRequest request) throws ApplicationException;
    FullServiceInfo update(Integer serviceId, Integer accountId, UpdateServiceRequest request) throws ApplicationException;
    void delete(Integer serviceId, Integer accountId) throws ApplicationException;
    PaginationServiceInfo findServiceInfoByShopIdAndStatus(Integer shopId, ServiceStatus status, Integer page, Integer size) throws ApplicationException;
    PaginationServiceInfo findByNameAndStatusIn(String name, List<ServiceStatus> statusList, Integer page, Integer size) throws ApplicationException;
    ListVerifyResponse getListVerifyResponse(Integer page, Integer size);
    void verify(UpdateVerifyStatusRequest request, Integer accountId) throws ApplicationException;
    ServiceStatus getServiceStatus(String status);
    void updateStatus(Integer serviceId, ServiceStatus status, Integer accountId) throws ApplicationException;
    List<SubServiceEntity> findByIdIn(List<Integer> ids);
    Page<MainServiceEntity> findByModifiedDate(Integer page, Integer size, String sortType, List<ServiceStatus> statuses);
    void updateRate(UpdateRateRequest request) throws ApplicationException;
    Page<MainServiceEntity> findByCategoryAndStatusIn(Integer categoryId, Integer page, Integer size, List<ServiceStatus> statuses);
    Page<MainServiceEntity> findByStatusAndRateAverage(ServiceStatus status, Float rateAverage, Integer page, Integer size);
    List<MainServiceEntity> filter(FilterRequest request);

}

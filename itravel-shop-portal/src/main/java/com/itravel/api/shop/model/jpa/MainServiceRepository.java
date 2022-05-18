package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.enums.ServiceStatus;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainServiceRepository extends PagingAndSortingRepository<MainServiceEntity, Integer>, MainServiceCustomRepository{
    Page<MainServiceEntity> findByShop_Id(Integer shopId, Pageable pageable);
    Page<MainServiceEntity> findByShop_IdAndStatus(Integer shopId, ServiceStatus status, Pageable pageable);
    List<MainServiceEntity> findAllByShop_Id(Integer shopId);
    Page<MainServiceEntity> findByTitleContainsAndStatusIn(String name, List<ServiceStatus> shopStatusList, Pageable pageable);
    Page<MainServiceEntity> findByStatusIn(List<ServiceStatus> statuses, Pageable pageable);
    Page<MainServiceEntity> findByStatus(ServiceStatus status, Pageable pageable);
    Page<MainServiceEntity> findByShop_IdAndStatusIn(Integer shopId, List<ServiceStatus> statuses, Pageable pageable);
    Page<MainServiceEntity> findByCategoryIdAndStatusIn(Integer categoryId, List<ServiceStatus> statuses, Pageable pageable);
    Page<MainServiceEntity> findByStatusAndRateAverageGreaterThanEqual(ServiceStatus status, Float rateAverage, Pageable pageable);
}

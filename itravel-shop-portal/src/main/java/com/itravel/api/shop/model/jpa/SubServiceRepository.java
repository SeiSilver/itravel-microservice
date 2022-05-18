package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.model.entity.SubServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubServiceRepository extends JpaRepository<SubServiceEntity, Integer> {
    List<SubServiceEntity> findByIdIn(List<Integer> ids);
    List<SubServiceEntity> findByMainServiceIdOrderByPriceDesc(Integer mainServiceId);
    List<SubServiceEntity> findByMainServiceIdOrderByPriceAsc(Integer mainServiceId);
}

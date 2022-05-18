package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.model.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {

}

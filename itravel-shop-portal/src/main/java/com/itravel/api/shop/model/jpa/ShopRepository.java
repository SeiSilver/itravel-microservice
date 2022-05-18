package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.enums.ShopStatus;
import com.itravel.api.shop.model.entity.ShopEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {
  Optional<ShopEntity> findByOwnerId(Integer ownerId);
  Page<ShopEntity> findByShopNameContains(String name, Pageable pageable);
  Page<ShopEntity> findByStatusIn(List<ShopStatus> statuses, Pageable pageable);
}

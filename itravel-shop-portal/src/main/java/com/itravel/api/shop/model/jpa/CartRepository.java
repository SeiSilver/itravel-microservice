package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.model.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItemEntity, Integer>{
    List<CartItemEntity> findByAccountId(Integer accountId);
}

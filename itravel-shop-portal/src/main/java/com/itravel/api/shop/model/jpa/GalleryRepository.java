package com.itravel.api.shop.model.jpa;

import com.itravel.api.shop.model.entity.GalleryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Integer> {
    List<GalleryEntity> findByObjectTypeAndObjectId(String objectType, Integer objectId);
    Optional<GalleryEntity> findFirstByObjectTypeAndObjectId(String objectType, Integer objectId);
}

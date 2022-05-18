package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.exception.S3UploadErrorException;
import com.itravel.api.shop.model.entity.GalleryEntity;

import java.util.List;

public interface GalleryService {
    List<String> findByObjectTypeAndId(String objectType, Integer objectId);
    GalleryEntity findFirstByObjectTypeAndId(String objectType, Integer objectId) throws ApplicationException;
    List<String> saveGalleries(Integer objectId, String objectType, List<String> images);
    List<String> saveImages(Integer serviceId, String objectType, List<String> requestImages) throws S3UploadErrorException;
}

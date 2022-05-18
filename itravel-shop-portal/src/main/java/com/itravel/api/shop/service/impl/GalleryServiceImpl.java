package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.constraint.S3Folder;
import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.exception.S3UploadErrorException;
import com.itravel.api.shop.model.dto.ArchiveDTO;
import com.itravel.api.shop.model.entity.GalleryEntity;
import com.itravel.api.shop.model.jpa.GalleryRepository;
import com.itravel.api.shop.service.GalleryService;
import com.itravel.api.shop.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class GalleryServiceImpl implements GalleryService {
    private final GalleryRepository galleryRepository;
    private final S3Service s3Service;

    @Override
    public List<String> findByObjectTypeAndId(String objectType, Integer objectId) {
        List<GalleryEntity> galleryEntities = galleryRepository.findByObjectTypeAndObjectId(objectType, objectId);
        List<String> images = new ArrayList<>();
        for(GalleryEntity galleryEntity : galleryEntities)
            images.add(galleryEntity.getImageLink());
        return images;
    }

    @Override
    public GalleryEntity findFirstByObjectTypeAndId(String objectType, Integer objectId) throws ApplicationException {
        Optional<GalleryEntity> opt = galleryRepository.findFirstByObjectTypeAndObjectId(objectType, objectId);
        if(opt.isEmpty()) {
            log.error("Gallery not found with objectType: {} and objectId: {}", objectType, objectId);
            throw new ApplicationException(ErrorCode.GALLERY_NOT_FOUND, ErrorMessage.GALLERY_NOT_FOUND);
        }else
            return opt.get();
    }

    @Override
    public List<String> saveGalleries(Integer objectId, String objectType, List<String> images) {
        List<String> rs = new ArrayList<>();
        for (String image : images) {
            GalleryEntity galleryEntity = galleryRepository.save(
                GalleryEntity.builder()
                    .objectId(objectId)
                    .objectType(objectType)
                    .imageLink(image)
                    .build()
            );
            rs.add(galleryEntity.getImageLink());
        }
        return rs;
    }

    @Override
    public List<String> saveImages(Integer serviceId, String objectType, List<String> requestImages) throws S3UploadErrorException {
        List<String> images = new ArrayList<>();
        for (String image : requestImages){
            images.add(s3Service.uploadImage(
                ArchiveDTO.builder()
                    .folderName(S3Folder.MAIN_SERVICE)
                    .fileContent(image)
                    .build()));
        }
        return saveGalleries(serviceId, objectType, images);
    }
}

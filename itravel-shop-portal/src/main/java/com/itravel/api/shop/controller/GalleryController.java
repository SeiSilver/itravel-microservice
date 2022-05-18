package com.itravel.api.shop.controller;

import com.itravel.api.shop.enums.GalleryObjectType;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.GalleryEntity;
import com.itravel.api.shop.payload.GetImageByListServiceRequest;
import com.itravel.api.shop.service.GalleryService;
import com.itravel.api.shop.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.GALLERY;

@RequiredArgsConstructor
@RestController()
public class GalleryController {
    private final GalleryService galleryService;
    private final PayloadUtils payloadUtils;

    @PostMapping(GALLERY)
    public ResponseEntity<Map<Integer, String>> getGalleryByList(
        @RequestBody GetImageByListServiceRequest request
        ) throws ApplicationException {
        payloadUtils.validateRequiredFields(request);
        Map<Integer, String> map = new HashMap<>();

        for(Integer serviceId : request.getServiceIds()){
            GalleryEntity galleryEntity = galleryService.findFirstByObjectTypeAndId(
                GalleryObjectType.SERVICE.name(),
                serviceId
            );
            map.put(serviceId, galleryEntity.getImageLink());
        }
        return ResponseEntity.ok(map);
    }
}

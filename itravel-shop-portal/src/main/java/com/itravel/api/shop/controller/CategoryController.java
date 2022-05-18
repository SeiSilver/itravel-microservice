package com.itravel.api.shop.controller;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.CategoryInfo;
import com.itravel.api.shop.model.entity.CategoryEntity;
import com.itravel.api.shop.payload.ListCategoryInfo;
import com.itravel.api.shop.service.CategoryService;
import com.itravel.api.shop.util.ModelMapperUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.CATEGORY;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.CATEGORY_ID;

@RequiredArgsConstructor
@RequestMapping(CATEGORY)
@RestController
@Log4j2
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapperUtils mapperUtils;

    @GetMapping
    @ApiOperation(value = "Get all category")
    public ResponseEntity<ListCategoryInfo> getAllCategory(){
        log.info("Receive get all category info request");
        List<CategoryEntity> categoryEntities = categoryService.findAll();
        return ResponseEntity.ok(
            ListCategoryInfo.builder()
                .categoryInfos(mapperUtils.mapList(categoryEntities, CategoryInfo.class))
                .build()
        );
    }

    @GetMapping(CATEGORY_ID)
    @ApiOperation(value = "Get category info by id")
    public ResponseEntity<CategoryInfo> findById(
        @PathVariable Integer categoryId
    ) throws ApplicationException {
        log.info("Receive category info request for id {}", categoryId);
        CategoryEntity categoryEntity = categoryService.findById(categoryId);
        return ResponseEntity.ok(mapperUtils.map(categoryEntity, CategoryInfo.class));
    }
}

package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.CategoryEntity;
import com.itravel.api.shop.model.jpa.CategoryRepository;
import com.itravel.api.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public CategoryEntity findById(Integer id) throws ApplicationException {
    return categoryRepository.findById(id).orElseThrow(
        () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND, ErrorMessage.CATEGORY_NOT_FOUND)
    );
  }

  @Override
  public List<CategoryEntity> findAll() {
    return categoryRepository.findAll();
  }
}

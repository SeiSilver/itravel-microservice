package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
  CategoryEntity findById(Integer id) throws ApplicationException;
  List<CategoryEntity> findAll();
}

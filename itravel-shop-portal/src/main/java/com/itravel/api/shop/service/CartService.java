package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.CartItemInfo;
import com.itravel.api.shop.model.entity.CartItemEntity;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import com.itravel.api.shop.payload.CartRequest;
import com.itravel.api.shop.payload.UpdateCartItemRequest;
import com.itravel.api.shop.payload.UpdateListCartItemRequest;

import java.time.ZonedDateTime;
import java.util.List;

public interface CartService {
    CartItemEntity create(Integer accountId, ZonedDateTime dateStart, Integer quantity, SubServiceEntity subServiceEntity);
    CartItemInfo findById(Integer cartItemId, Integer accountId)  throws ApplicationException;
    List<CartItemInfo> findByAccountId(Integer accountId) throws ApplicationException;
    CartItemInfo update(Integer accountId, Integer cartItemId, UpdateCartItemRequest request) throws ApplicationException;
    void checkOwner(Integer ownerId, Integer accountId) throws ApplicationException;
    void delete(Integer accountId, List<Integer> cartIds) throws ApplicationException;
    void add(Integer accountId, CartRequest request) throws ApplicationException;
    List<CartItemInfo> updateListCart(UpdateListCartItemRequest request) throws ApplicationException;
}

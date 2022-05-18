package com.itravel.api.shop.service;

import com.itravel.api.shop.enums.ShopStatus;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.ShopInfo;
import com.itravel.api.shop.model.entity.AccountEntity;
import com.itravel.api.shop.model.entity.ShopEntity;
import com.itravel.api.shop.payload.CreateShopRequest;
import com.itravel.api.shop.payload.MainServiceInfoPageable;
import com.itravel.api.shop.payload.PaginationShopInfo;
import com.itravel.api.shop.payload.ShopOnlyInfo;
import com.itravel.api.shop.payload.UpdateShopRequest;

import java.util.List;

public interface ShopService {
    ShopEntity findById(Integer shopId) throws ApplicationException;
    ShopOnlyInfo findShopOnlyInfo(Integer shopId) throws ApplicationException;
    PaginationShopInfo findShopOnlyInfoByShopName(String shopName, Integer page, Integer size);
    PaginationShopInfo findShopOnlyInfoByStatus(List<ShopStatus> statuses, Integer page, Integer size);
    Boolean existByOwnerId(Integer ownerId);
    ShopOnlyInfo findByOwnerId(Integer ownerId) throws ApplicationException;
    ShopInfo update(Integer shopId, Integer accountId, String ownerName, UpdateShopRequest request) throws ApplicationException;
    void checkOwner(Integer ownerId, Integer accountId) throws ApplicationException;
    ShopInfo create(AccountEntity accountEntity, String ownerName, CreateShopRequest request) throws ApplicationException;
    void delete(Integer shopId, Integer accountId) throws ApplicationException;
    MainServiceInfoPageable findServiceInfoByShopId(Integer shopId, Integer page, Integer size, boolean isMod, Integer accountId) throws ApplicationException;
    PaginationShopInfo findAllShop(Integer page, Integer size);
    void updateStatus(Integer shopId, ShopStatus status, Integer accountId) throws ApplicationException;
    Boolean isOwner(Integer accountId, Integer shopId) throws ApplicationException;
    Boolean isOwnerOtherShop(Integer accountId);
}

package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.ShopInfo;
import com.itravel.api.shop.model.entity.AccountEntity;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.model.entity.ShopEntity;
import com.itravel.api.shop.enums.ShopStatus;
import com.itravel.api.shop.model.jpa.MainServiceRepository;
import com.itravel.api.shop.model.jpa.ShopRepository;
import com.itravel.api.shop.payload.CreateNotificationRequestPayload;
import com.itravel.api.shop.payload.CreateShopRequest;
import com.itravel.api.shop.model.dto.MainServiceInfo;
import com.itravel.api.shop.payload.MainServiceInfoPageable;
import com.itravel.api.shop.payload.PaginationShopInfo;
import com.itravel.api.shop.payload.ShopOnlyInfo;
import com.itravel.api.shop.payload.UpdateShopRequest;
import com.itravel.api.shop.service.MessagingService;
import com.itravel.api.shop.service.ShopService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.itravel.api.shop.util.ModelMapperUtils;
import com.itravel.api.shop.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.itravel.api.shop.constraint.DefaultValue.NOTIFY_SHOP_STATUS_CHANGE_TITLE;
import static com.itravel.api.shop.constraint.DefaultValue.NOTIFY_SHOP_STATUS_CHANGE_MESSAGE;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;
    private final MainServiceRepository mainRepository;
    private final PayloadUtils payloadUtils;
    private final ModelMapperUtils mapperUtils;
    private final MessagingService messagingService;

    public ShopOnlyInfo findByOwnerId(Integer ownerId) throws ApplicationException {
        ShopEntity shopEntity= shopRepository.findByOwnerId(ownerId).orElseThrow(
            () -> new ApplicationException(ErrorCode.NOT_HAVE_SHOP, ErrorMessage.NOT_HAVE_SHOP)
        );
        ShopOnlyInfo shopInfo = mapperUtils.map(shopEntity, ShopOnlyInfo.class);

        List<MainServiceEntity> serviceEntities = findServiceByShopId(shopEntity.getId());
        shopInfo.setServiceCount(serviceEntities.size());
    return shopInfo;
  }

    @Override
    public ShopOnlyInfo findShopOnlyInfo(Integer shopId) throws ApplicationException {
        ShopEntity shopEntity = findById(shopId);
        ShopOnlyInfo shopInfo = mapperUtils.map(shopEntity, ShopOnlyInfo.class);

        List<MainServiceEntity> serviceEntities = findServiceByShopId(shopId);
        shopInfo.setServiceCount(serviceEntities.size());
        return shopInfo;
    }

    @Override
    public PaginationShopInfo findShopOnlyInfoByShopName(String shopName, Integer page, Integer size){
        Page<ShopEntity> pages = shopRepository.findByShopNameContains(shopName, PageRequest.of(page-1, size));
        return toPaginationResponse(pages, page, size);
    }

    PaginationShopInfo toPaginationResponse(Page<ShopEntity> pages, Integer page, Integer size){
        return PaginationShopInfo.builder()
            .page(page)
            .size(size)
            .totalPage(pages.getTotalPages())
            .totalItem(pages.getTotalElements())
            .shopInfos(mapperUtils.mapList(pages.getContent(), ShopOnlyInfo.class))
            .build();
    }

    @Override
    public PaginationShopInfo findShopOnlyInfoByStatus(List<ShopStatus> statuses, Integer page, Integer size) {
        Page<ShopEntity> pages = shopRepository.findByStatusIn(statuses, PageRequest.of(page-1, size));
        return toPaginationResponse(pages, page, size);
    }

    List<MainServiceEntity> findServiceByShopId(Integer shopId){
      return mainRepository.findAllByShop_Id(shopId);
    }

    public ShopEntity findById(Integer shopId) throws ApplicationException {
    return shopRepository.findById(shopId).orElseThrow(
        () -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND, ErrorMessage.SHOP_NOT_FOUND)
    );
  }

  @Transactional
  public ShopEntity createShop(AccountEntity accountEntity, String ownerName, CreateShopRequest request){
    ShopEntity shop = ShopEntity.builder()
        .ownerId(Math.toIntExact(accountEntity.getId()))
        .ownerName(ownerName)
        .shopName(request.getShopName())
        .address(request.getAddress())
        .phoneNumber(accountEntity.getPhoneNumber())
        .description(request.getDescription())
        .status(ShopStatus.ACTIVE)
        .build();
    shop = shopRepository.save(shop);
    return shop;
  }

  @Transactional
  public ShopEntity updateShop(ShopEntity shopEntity, String ownerName, UpdateShopRequest request) {
     return shopRepository.save(
             ShopEntity.builder()
             .id(shopEntity.getId())
             .ownerId(shopEntity.getOwnerId())
                 .ownerName(ownerName)
             .shopName(request.getShopName())
             .address(request.getAddress())
             .description(request.getDescription())
             .phoneNumber(request.getPhoneNumber())
             .services(shopEntity.getServices())
             .status(request.getStatus())
             .createdAt(shopEntity.getCreatedAt())
             .build());

  }

    @Override
    public ShopInfo update(Integer shopId, Integer accountId, String ownerName, UpdateShopRequest request) throws ApplicationException {
        ShopEntity shopEntity = findById(shopId);
        checkOwner(shopEntity.getOwnerId(), accountId);
        shopEntity = updateShop(shopEntity, ownerName, request);
        return mapperUtils.map(shopEntity, ShopInfo.class);
    }

    public void updateStatus(ShopEntity shopEntity, ShopStatus status){
        shopEntity.setStatus(status);
        shopRepository.save(shopEntity);
  }

  public Boolean existByOwnerId(Integer ownerId) {
    Optional<ShopEntity> optional = shopRepository.findByOwnerId(ownerId);
    return optional.isPresent();
  }

  @Override
  public void checkOwner(Integer ownerId, Integer accountId) throws ApplicationException {
    if(ownerId.compareTo(accountId) != 0)
      throw new ApplicationException(ErrorCode.NOT_SHOP_OWNER, ErrorMessage.NOT_SHOP_OWNER);
  }

    @Override
    public ShopInfo create(AccountEntity accountEntity, String ownerName, CreateShopRequest request) throws ApplicationException {

        ShopEntity shopEntity = createShop(accountEntity, ownerName, request);
        return mapperUtils.map(shopEntity, ShopInfo.class);
    }

    @Override
    public void delete(Integer shopId, Integer accountId) throws ApplicationException {
        ShopEntity shopEntity = findById(shopId);
        checkOwner(shopEntity.getOwnerId(), accountId);
        updateStatus(shopEntity, ShopStatus.DELETED);
    }

    @Override
    public MainServiceInfoPageable findServiceInfoByShopId(Integer shopId, Integer page, Integer size, boolean isMod, Integer accountId)
        throws ApplicationException {
        if(!isMod) {
            ShopEntity shopEntity = findById(shopId);
            checkOwner(shopEntity.getOwnerId(), accountId);
        }

        Page<MainServiceEntity> serviceEntities = mainRepository.findByShop_Id(shopId, PageRequest.of(page-1, size));
        List<MainServiceInfo> mainServiceInfos = payloadUtils.toListInfo(serviceEntities.getContent());
        return MainServiceInfoPageable.builder()
            .page(page)
            .totalResults((int) serviceEntities.getTotalElements())
            .totalPages(serviceEntities.getTotalPages())
            .results(mainServiceInfos)
            .build();
    }

    @Override
    public PaginationShopInfo findAllShop(Integer page, Integer size) {
        Page<ShopEntity> pages = shopRepository.findAll(PageRequest.of(page-1,size));
        return toPaginationResponse(pages, page, size);
    }

    @Override
    public void updateStatus(Integer shopId, ShopStatus status, Integer accountId) throws ApplicationException {
        ShopEntity shopEntity = findById(shopId);
        updateStatus(shopEntity, status);
        CreateNotificationRequestPayload payload = CreateNotificationRequestPayload.builder()
            .receiverId(Long.valueOf(shopEntity.getOwnerId()))
            .title(NOTIFY_SHOP_STATUS_CHANGE_TITLE)
            .message(String.format(NOTIFY_SHOP_STATUS_CHANGE_MESSAGE, shopId, status.name(), accountId))
            .createdAt(ZonedDateTime.now())
        .build();
        messagingService.sendNewNotificationRequestToQueue(payload);
    }

    @Override
    public Boolean isOwner(Integer accountId, Integer shopId) throws ApplicationException {
        ShopEntity shopEntity = findById(shopId);
        return shopEntity.getOwnerId().compareTo(accountId)==0;
    }

    @Override
    public Boolean isOwnerOtherShop(Integer accountId) {
        return existByOwnerId(accountId);
    }

}

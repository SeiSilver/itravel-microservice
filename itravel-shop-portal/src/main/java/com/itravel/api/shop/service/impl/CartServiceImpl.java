package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.enums.ShopStatus;
import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.CartItemInfo;
import com.itravel.api.shop.model.entity.CartItemEntity;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import com.itravel.api.shop.model.jpa.CartRepository;
import com.itravel.api.shop.payload.*;
import com.itravel.api.shop.service.CartService;
import com.itravel.api.shop.service.GalleryService;
import com.itravel.api.shop.service.SubService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import com.itravel.api.shop.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.itravel.api.shop.enums.GalleryObjectType.SERVICE;
import static com.itravel.api.shop.enums.ServiceStatus.ACTIVE;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final ModelMapper mapper;
  private final SubService subService;
  private final DateTimeUtils dateTimeUtils;
  private final GalleryService galleryService;

  @Override
  @Transactional
  public CartItemEntity create(Integer accountId, ZonedDateTime usedStart, Integer quantity, SubServiceEntity subServiceEntity) {
    return cartRepository.save(
        CartItemEntity.builder()
            .accountId(accountId)
            .quantity(quantity)
            .usedStart(usedStart)
            .usedEnd(usedStart.plusDays(subServiceEntity.getMainService().getDuration()))
            .subService(subServiceEntity)
            .build());
  }

  @Override
  public CartItemInfo findById(Integer cartItemId, Integer accountId) throws ApplicationException {
    CartItemEntity cartItem = findEntityById(cartItemId);
    checkOwner(cartItem.getAccountId(), accountId);
    return mapper.map(cartItem, CartItemInfo.class);
  }

  private CartItemEntity findEntityById(Integer id) throws ApplicationException {
    return cartRepository.findById(id).orElseThrow(
        () -> {
          log.error("Cart item not found with id: {}", id);
          return new ApplicationException(ErrorCode.CART_ITEM_NOT_FOUND, ErrorMessage.CART_ITEM_NOT_FOUND);
        }
    );
  }

  private List<CartItemEntity> findEntityByAccountId(Integer accountId) {
    return cartRepository.findByAccountId(accountId);
  }

  @Override
  public List<CartItemInfo> findByAccountId(Integer accountId) throws ApplicationException {
    List<CartItemEntity> cartItems = findEntityByAccountId(accountId);
    List<CartItemInfo> cartItemInfos = new ArrayList<>();
    for (CartItemEntity cartItem : cartItems) {
      cartItemInfos.add(toInfo(cartItem));
    }
    return cartItemInfos;
  }

  CartItemInfo toInfo(CartItemEntity entity) throws ApplicationException {
    CartItemInfo cartItemInfo = mapper.map(entity, CartItemInfo.class);
    cartItemInfo.setPrice(entity.getSubService().getPrice());
    cartItemInfo.setImage(getCartImage(cartItemInfo.getMainServiceId()));
    return cartItemInfo;
  }

    public String getCartImage(Integer serviceId) throws ApplicationException {
        return galleryService.findFirstByObjectTypeAndId(SERVICE.name(), serviceId).getImageLink();
    }

  @Override
  @Transactional
  public CartItemInfo update(Integer accountId, Integer cartItemId, UpdateCartItemRequest request) throws ApplicationException {

    CartItemEntity cartItem = findEntityById(cartItemId);
    checkOwner(cartItem.getAccountId(), accountId);
    cartItem = updateEntity(cartItem, request.getQuantity(), request.getUsedStart());
    cartRepository.save(cartItem);
    CartItemInfo cartItemInfo = mapper.map(cartItem, CartItemInfo.class);
    cartItemInfo.setImage(getCartImage(cartItemInfo.getMainServiceId()));
    return cartItemInfo;
  }

  @Override
  public void checkOwner(Integer ownerId, Integer accountId) throws ApplicationException {
    if (ownerId.compareTo(accountId) != 0) {
      throw new ApplicationException(ErrorCode.NOT_CART_OWNER, ErrorMessage.NOT_CART_OWNER);
    }
  }

  public void deleteCartItem(CartItemEntity cartItemEntity) {
    cartRepository.delete(cartItemEntity);
  }

  @Override
  @Transactional
  public void delete(Integer accountId, List<Integer> cartIds) throws ApplicationException {
    for (Integer cartId : cartIds) {
      CartItemEntity cartItem = findEntityById(cartId);
      checkOwner(cartItem.getAccountId(), accountId);
      deleteCartItem(cartItem);
      log.info("Delete cart {} success", cartId);
    }
  }

  @Override
  @Transactional
  public void add(Integer accountId, CartRequest request) throws ApplicationException {
    for (CartItemRequest cartItemRequest : request.getCartItems()) {
      SubServiceEntity subServiceEntity = subService.findSubById(cartItemRequest.getSubServiceId());
      if(subServiceEntity.getStockAmount() < cartItemRequest.getQuantity())
        throw new ApplicationException(ErrorCode.NOT_ENOUGH_QUANTITY,
            String.format(ErrorMessage.NOT_ENOUGH_QUANTITY, subServiceEntity.getStockAmount(), cartItemRequest.getQuantity()));

      if(subServiceEntity.getStatus() != ACTIVE)
        throw new ApplicationException(ErrorCode.SUB_SERVICE_CAN_NOT_PROCESS,
            String.format(ErrorMessage.SUB_SERVICE_CAN_NOT_PROCESS, subServiceEntity.getId(), subServiceEntity.getStatus().name()));
      if(subServiceEntity.getMainService().getStatus() != ACTIVE)
        throw new ApplicationException(
            ErrorCode.SERVICE_CAN_NOT_PROCESS,
            String.format(ErrorMessage.SERVICE_CAN_NOT_PROCESS, subServiceEntity.getMainService().getId(), subServiceEntity.getMainService().getStatus().name())
        );
      if(subServiceEntity.getMainService().getShop().getStatus() != ShopStatus.ACTIVE)
        throw new ApplicationException(
            ErrorCode.SHOP_CAN_NOT_PROCESS,
            String.format(ErrorMessage.SHOP_CAN_NOT_PROCESS,
                subServiceEntity.getMainService().getShop().getId(),
                subServiceEntity.getMainService().getShop().getStatus().name())
        );

      if (subServiceEntity.getMainService().getStatus().equals(ACTIVE)) {
        create( accountId, dateTimeUtils.toZonedDate(request.getDateStart()), cartItemRequest.getQuantity(), subServiceEntity );
      }
    }
  }

    @Override
    @Transactional
    public List<CartItemInfo> updateListCart(UpdateListCartItemRequest request) throws ApplicationException {
        List<CartItemEntity> list = new ArrayList<>();
        List<CartItemInfo> rs = new ArrayList<>();
        for(CartItemsRequest cartItemsRequest : request.getRequests()){
            CartItemEntity cartItemEntity = findEntityById(cartItemsRequest.getCartItemId());
            cartItemEntity = updateEntity(cartItemEntity, cartItemsRequest.getQuantity(), cartItemsRequest.getUsedStart());
            list.add(cartItemEntity);
            rs.add(toInfo(cartItemEntity));
        }
        cartRepository.saveAll(list);
        return rs;
    }

      CartItemEntity updateEntity(CartItemEntity entity, Integer quantity, String usedDate){
          entity.setQuantity(quantity);
          entity.setUsedStart(dateTimeUtils.toZonedDate(usedDate));
          entity.setUsedEnd(entity.getUsedStart().plusDays(entity.getSubService().getMainService().getDuration()));
          return entity;
      }
}

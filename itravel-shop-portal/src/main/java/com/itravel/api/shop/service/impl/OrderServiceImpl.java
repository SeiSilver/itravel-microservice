package com.itravel.api.shop.service.impl;

import com.itravel.api.shop.enums.GalleryObjectType;
import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.OrderInfo;
import com.itravel.api.shop.model.dto.OrderItemInfo;
import com.itravel.api.shop.model.entity.CartItemEntity;
import com.itravel.api.shop.model.entity.GalleryEntity;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import com.itravel.api.shop.model.jpa.CartRepository;
import com.itravel.api.shop.model.jpa.SubServiceRepository;
import com.itravel.api.shop.payload.CreateOrderRequest;
import com.itravel.api.shop.payload.MQOrderRequest;
import com.itravel.api.shop.service.GalleryService;
import com.itravel.api.shop.service.MessagingService;
import com.itravel.api.shop.service.OrderService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.itravel.api.shop.util.ModelMapperUtils;
import com.itravel.api.shop.util.PayloadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final ModelMapperUtils mapperUtils;
    private final MessagingService messagingService;
    private final PayloadUtils payloadUtils;
    private final SubServiceRepository subServiceRepository;
    private final GalleryService galleryService;

    @Override
    @Transactional
    public String createOrder(Integer accountId, CreateOrderRequest request) throws ApplicationException {
        payloadUtils.validateRequiredFields(request);

        long totalPrice = 0L;
        List<OrderItemInfo> orderItemInfos = new ArrayList<>();
        List<CartItemEntity> cartItemEntities = new ArrayList<>();
        List<SubServiceEntity> subServiceEntities = new ArrayList<>();

        for (Integer id : request.getCartIds()) {
            CartItemEntity cartItem = cartRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.CART_ITEM_NOT_FOUND, ErrorMessage.CART_ITEM_NOT_FOUND)
            );
            SubServiceEntity subServiceEntity = cartItem.getSubService();
            MainServiceEntity mainService = subServiceEntity.getMainService();
            if(!isValidUsedDate(cartItem.getUsedStart(), mainService.getEventStart(), mainService.getEventEnd()))
                throw new ApplicationException(ErrorCode.INVALID_USED_DATE, String.format(ErrorMessage.INVALID_USED_DATE,
                    cartItem.getUsedStart(), mainService.getEventStart(), mainService.getEventEnd()));

            totalPrice += subServiceEntity.getPrice() * cartItem.getQuantity();
            subServiceEntity.setStockAmount(subServiceEntity.getStockAmount() - cartItem.getQuantity());

            subServiceEntities.add(subServiceEntity);
            cartItemEntities.add(cartItem);

            GalleryEntity galleryEntity = galleryService.findFirstByObjectTypeAndId(GalleryObjectType.SERVICE.name(), cartItem.getSubService().getMainService().getId());
            OrderItemInfo itemInfo = mapperUtils.mapToOrderItemInfo(cartItem, galleryEntity.getImageLink());
            orderItemInfos.add(itemInfo);
        }

        log.info("Create MQOrderRequest");
        MQOrderRequest mqOrderRequest = MQOrderRequest.builder()
            .order(OrderInfo.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .orderBillId(UUID.randomUUID().toString())
                .accountId(accountId)
                .totalPrice(totalPrice)
                .build())
            .orderItems(orderItemInfos)
            .build();
        log.info("Create MQOrderRequest success with payload: {}", mqOrderRequest);

        messagingService.sendNewOrderRequestToQueue(mqOrderRequest);
        subServiceRepository.saveAll(subServiceEntities);
        cartRepository.deleteAll(cartItemEntities);
        log.info("Used cart item deleted");
        return mqOrderRequest.getOrder().getOrderBillId();
    }

    public Boolean isValidUsedDate(ZonedDateTime usedStart, ZonedDateTime eventStart, ZonedDateTime eventEnd){
        return !usedStart.isBefore(eventStart) && !usedStart.isAfter(eventEnd);
    }
}

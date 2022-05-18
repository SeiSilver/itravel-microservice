package com.itravel.api.shop.util;

import static com.itravel.api.shop.enums.GalleryObjectType.SERVICE;

import com.itravel.api.shop.model.dto.FullServiceInfo;
import com.itravel.api.shop.model.dto.MainServiceInfo;
import com.itravel.api.shop.model.dto.OrderItemInfo;
import com.itravel.api.shop.model.entity.CartItemEntity;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.service.GalleryService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class ModelMapperUtils {

  private final ModelMapper modelMapper;
  private final GalleryService galleryService;

  public <T> T map(Object source, Class<T> targetClass) {
    return modelMapper.map(source, targetClass);
  }

  public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
    return source
        .stream()
        .map(element -> modelMapper.map(element, targetClass))
        .collect(Collectors.toList());
  }

  public List<FullServiceInfo> mapToFullServiceInfo(List<MainServiceEntity> list) {
    List<FullServiceInfo> serviceInfos = new ArrayList<>();
    for (MainServiceEntity mainService : list) {
      FullServiceInfo serviceInfo = modelMapper.map(mainService, FullServiceInfo.class);
      serviceInfo.setImages(galleryService.findByObjectTypeAndId(SERVICE.name(), mainService.getId()));
      serviceInfos.add(serviceInfo);
    }
    return serviceInfos;
  }

  public List<MainServiceInfo> mapToMainServiceInfo(List<MainServiceEntity> list){
    List<MainServiceInfo> serviceInfos = new ArrayList<>();
    for (MainServiceEntity mainService : list) {
      MainServiceInfo serviceInfo = modelMapper.map(mainService, MainServiceInfo.class);
      serviceInfo.setImages(galleryService.findByObjectTypeAndId(SERVICE.name(), mainService.getId()));
      serviceInfos.add(serviceInfo);
    }
    return serviceInfos;
  }

  public OrderItemInfo mapToOrderItemInfo(CartItemEntity cartItem, String image){
    return OrderItemInfo.builder()
        .id(cartItem.getId())
        .price(cartItem.getSubService().getPrice())
        .subServiceId(cartItem.getSubService().getId())
        .subServiceTitle(cartItem.getSubService().getTitle())
        .mainServiceId(cartItem.getSubService().getMainService().getId())
        .mainServiceTitle(cartItem.getSubService().getMainService().getTitle())
        .quantity(cartItem.getQuantity())
        .shopId(cartItem.getSubService().getMainService().getShop().getId())
        .shopName(cartItem.getSubService().getMainService().getShop().getShopName())
        .usedStart(cartItem.getUsedStart())
        .usedEnd(cartItem.getUsedEnd())
        .image(image)
        .build();
  }
}

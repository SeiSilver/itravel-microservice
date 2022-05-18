package com.itravel.api.shop.service.impl;

import static com.itravel.api.shop.constraint.DefaultValue.NOTIFY_SHOP_STATUS_CHANGE_MESSAGE;
import static com.itravel.api.shop.constraint.DefaultValue.NOTIFY_SHOP_STATUS_CHANGE_TITLE;
import static com.itravel.api.shop.constraint.DefaultValue.NOTIFY_SERVICE_STATUS_CHANGE_TITLE;
import static com.itravel.api.shop.constraint.DefaultValue.NOTIFY_SERVICE_STATUS_CHANGE_MESSAGE;
import static com.itravel.api.shop.constraint.DefaultValue.SUB_SERVICE_DELETE;
import static com.itravel.api.shop.constraint.DefaultValue.SUB_SERVICE_ID;
import static com.itravel.api.shop.constraint.DefaultValue.SUB_SERVICE_UPDATE;

import com.itravel.api.shop.constraint.DefaultValue;
import com.itravel.api.shop.enums.GalleryObjectType;
import com.itravel.api.shop.enums.ServiceStatus;
import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.exception.S3UploadErrorException;
import com.itravel.api.shop.model.dto.FullServiceInfo;
import com.itravel.api.shop.model.entity.CategoryEntity;
import com.itravel.api.shop.model.entity.CityEntity;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.model.entity.ShopEntity;
import com.itravel.api.shop.model.entity.SubServiceEntity;
import com.itravel.api.shop.model.jpa.CategoryRepository;
import com.itravel.api.shop.model.jpa.CityRepository;
import com.itravel.api.shop.model.jpa.MainServiceRepository;
import com.itravel.api.shop.model.jpa.ShopRepository;
import com.itravel.api.shop.model.jpa.SubServiceRepository;
import com.itravel.api.shop.payload.*;
import com.itravel.api.shop.service.GalleryService;
import com.itravel.api.shop.service.ITravelService;
import com.itravel.api.shop.service.MessagingService;
import com.itravel.api.shop.util.DateTimeUtils;
import com.itravel.api.shop.util.ModelMapperUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.itravel.api.shop.enums.ServiceStatus.ACTIVE;
import static com.itravel.api.shop.enums.ServiceStatus.BLOCKED;
import static com.itravel.api.shop.enums.ServiceStatus.DELETED;
import static com.itravel.api.shop.enums.ServiceStatus.REJECT;
import static com.itravel.api.shop.enums.ServiceStatus.UNAVAILABLE;
import static com.itravel.api.shop.enums.ServiceStatus.WAITING;

@Service
@RequiredArgsConstructor
public class ITravelServiceImpl implements ITravelService {

  private final MainServiceRepository mainRepository;
  private final SubServiceRepository subRepository;
  private final ShopRepository shopRepository;
  private final CityRepository cityRepository;
  private final CategoryRepository categoryRepository;
  private final GalleryService galleryService;
  private final ModelMapperUtils mapperUtils;
  private final DateTimeUtils dateTimeUtils;
  private final MessagingService messagingService;

  private void checkShopOwner(Integer ownerId, Integer accountId) throws ApplicationException {
    if (ownerId.compareTo(accountId) != 0) {
      throw new ApplicationException(ErrorCode.NOT_SHOP_OWNER, ErrorMessage.NOT_SHOP_OWNER);
    }
  }

  private ShopEntity findShopById(Integer shopId) throws ApplicationException {
    return shopRepository.findById(shopId).orElseThrow(
        () -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND, ErrorMessage.SHOP_NOT_FOUND)
    );
  }

  private CategoryEntity findCategoryById(Integer id) throws ApplicationException {
    return categoryRepository.findById(id).orElseThrow(
        () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND, ErrorMessage.CATEGORY_NOT_FOUND)
    );
  }

  private CityEntity findCityById(Integer id) throws ApplicationException {
    return cityRepository.findById(id).orElseThrow(
        () -> new ApplicationException(ErrorCode.CITY_NOT_FOUND, ErrorMessage.CITY_NOT_FOUND)
    );
  }

    @Transactional
    public MainServiceEntity createMainService(
        ShopEntity shopEntity, CityEntity cityEntity,
        CategoryEntity categoryEntity, CreateServiceRequest request) throws ApplicationException {
        MainServiceEntity mainService = mainRepository.save(
            MainServiceEntity.builder()
                .shop(shopEntity)
                .city(cityEntity)
                .category(categoryEntity)
                .duration(request.getDuration())
                .eventStart(dateTimeUtils.toZonedDate(request.getEventStart()))
                .eventEnd(dateTimeUtils.toZonedDate(request.getEventEnd()))
                .description(request.getDescription())
                .title(request.getTitle())
                .address(request.getAddress())
                .status(WAITING)
            .build()
        );
    uploadServiceImages(mainService.getId(), request.getImages());
    mainService.setSubServices(createSubServices(mainService, request.getSubServices()));
    mainService = mainRepository.save(mainService);
//send notify to mod
    return mainService;
  }

    public List<String> uploadServiceImages(Integer serviceId, List<String> images) throws S3UploadErrorException {
        if(images == null || images.size()==0)
          images = List.of(DefaultValue.SERVICE_IMAGE);
        return galleryService.saveImages(serviceId, GalleryObjectType.SERVICE.name(), images);
    }

  private List<SubServiceEntity> createSubServices(MainServiceEntity mainService, List<CreateSubServiceRequest> requests) throws ApplicationException {
    List<SubServiceEntity> subServices = new ArrayList<>();
    for (CreateSubServiceRequest subRequest : requests) {
      validateServicePrice(subRequest.getPrice());
      SubServiceEntity subServiceEntity = subRepository.save(
          SubServiceEntity.builder()
              .mainService(mainService)
              .title(subRequest.getTitle())
              .stockAmount(subRequest.getStockAmount())
              .price(subRequest.getPrice())
              .build()
      );
      subServices.add(subServiceEntity);
    }
    return subServices;
  }

    public void validateServicePrice(Long price) throws ApplicationException{
        if(price < 10000)
            throw new ApplicationException(ErrorCode.SERVICE_PRICE_INVALID, String.format(ErrorMessage.SERVICE_PRICE_INVALID, price));
    }

  @Override
  public Optional<MainServiceEntity> findOptionalById(Integer id) {
    return mainRepository.findById(id);
  }

  @Override
  public MainServiceEntity findEntityById(Integer id) throws ApplicationException {

    return mainRepository.findById(id).orElseThrow(
        () -> new ApplicationException(
            ErrorCode.MAIN_SERVICE_NOT_FOUND,
            ErrorMessage.MAIN_SERVICE_NOT_FOUND
        )
    );
  }


  @Override
  public PaginationServiceInfo findAll(Integer page, Integer size, List<ServiceStatus> statuses) {
    Page<MainServiceEntity> pages = mainRepository.findByStatusIn(statuses, PageRequest.of(page-1, size));
    return toPagination(pages, page, size);
  }

  @Override
  public PaginationServiceInfo findByShopIdAndListStatus(Integer shopId, List<ServiceStatus> statuses, Integer page, Integer size){
    Page<MainServiceEntity> pages;
    if(shopId == null || shopId == 0)
      pages = mainRepository.findByStatusIn(statuses, PageRequest.of(page-1, size));
    else
      pages = mainRepository.findByShop_IdAndStatusIn(shopId, statuses, PageRequest.of(page-1, size));
    return toPagination(pages, page, size);
  }


  public PaginationServiceInfo toPagination(Page<MainServiceEntity> pages, Integer page, Integer size){
    return PaginationServiceInfo.builder()
        .page(page)
        .size(size)
        .totalPage(pages.getTotalPages())
        .totalItem(pages.getTotalElements())
        .serviceInfos(mapperUtils.mapToMainServiceInfo(pages.getContent()))
        .build();
  }

  @Override
  public Page<MainServiceEntity> findEntityByShopId(Integer id, Integer page, Integer size) {
    return mainRepository.findByShop_Id(id, PageRequest.of(page-1, size));
  }

  private MainServiceEntity update(MainServiceEntity mainService, CityEntity cityEntity, CategoryEntity categoryEntity, UpdateServiceRequest request)
      throws ApplicationException, DateTimeParseException {
    mainService.setDescription(request.getDescription());
    mainService.setTitle(request.getTitle());
    mainService.setAddress(request.getAddress());
    mainService.setStatus(request.getStatus());
    mainService.setCity(cityEntity);
    mainService.setCategory(categoryEntity);
    mainService.setDuration(request.getDuration());
    mainService.setEventStart(dateTimeUtils.toZonedDate(request.getEventStart()));
    mainService.setEventEnd(dateTimeUtils.toZonedDate(request.getEventEnd()));

    uploadServiceImages(mainService.getId(), request.getImages());
    List<SubServiceEntity> subServices = new ArrayList<>();

    SubServiceEntity subServiceEntity;
    for (UpdateSubServiceRequest subRequest : request.getSubServices()) {
      if (subRequest.getId().compareTo(SUB_SERVICE_ID) == 0) {
        subServiceEntity = createSubService(subRequest, mainService);
        subServices.add(subServiceEntity);
        mainService = updatePrice(subServiceEntity);
      } else if (subRequest.getAction().equalsIgnoreCase(SUB_SERVICE_UPDATE)) {
        subServiceEntity = updateSubService(subRequest, mainService);
        subServices.add(subServiceEntity);
        mainService = updatePrice(subServiceEntity);
      } else if (subRequest.getAction().equalsIgnoreCase(SUB_SERVICE_DELETE)) {
        deleteSubService(subRequest.getId());
        mainService.setLowestPrice(getLowestPrice(mainService.getId()));
      }
    }
    mainService.setSubServices(subServices);
    mainService.setStatus(WAITING);
    return mainRepository.save(mainService);
  }

  private MainServiceEntity updatePrice(SubServiceEntity subServiceEntity){
    MainServiceEntity mainService = subServiceEntity.getMainService();
    if (subServiceEntity.getPrice() < mainService.getLowestPrice())
      mainService.setLowestPrice(subServiceEntity.getPrice());
    return mainService;
  }

  private SubServiceEntity createSubService(UpdateSubServiceRequest request, MainServiceEntity mainService) {
    return subRepository.save(
        SubServiceEntity.builder()
            .mainService(mainService)
            .title(request.getTitle())
            .price(request.getPrice())
            .stockAmount(request.getStockAmount())
            .status(ACTIVE)
            .build()
    );
  }

  private SubServiceEntity updateSubService(UpdateSubServiceRequest request, MainServiceEntity mainService) throws ApplicationException {
    SubServiceEntity subService = findSubById(request.getId());

    // can not edit deleted service
    if (subService.getStatus() == ServiceStatus.DELETED || subService.getStatus() == ServiceStatus.BLOCKED) {
      return subService;
    }

    subService = SubServiceEntity.builder()
        .id(request.getId())
        .mainService(mainService)
        .title(request.getTitle())
        .stockAmount(request.getStockAmount())
        .price(request.getPrice())
        .status(WAITING)
        .createdAt(subService.getCreatedAt())
        .build();
    return subRepository.save(subService);
  }

  private void deleteSubService(Integer id) throws ApplicationException {
    SubServiceEntity subService = findSubById(id);
    subService.setStatus(DELETED);
    subRepository.save(subService);
  }

  private void updateStatus(MainServiceEntity mainServiceEntity, ServiceStatus status) {
    mainServiceEntity.setStatus(status);
    mainRepository.save(mainServiceEntity);
  }

  @Override
  public SubServiceEntity findSubById(Integer id) throws ApplicationException {
    return subRepository.findById(id).orElseThrow(
        () -> new ApplicationException(
            ErrorCode.SUB_SERVICE_NOT_FOUND,
            ErrorMessage.SUB_SERVICE_NOT_FOUND
        )
    );
  }

  @Override
  public FullServiceInfo findServiceInfoById(Integer accountId, Integer serviceId, List<ServiceStatus> statuses) throws ApplicationException {
    MainServiceEntity mainServiceEntity = findEntityById(serviceId);
    if(isServiceOwner(accountId, mainServiceEntity.getShop().getOwnerId())){
      statuses.add(WAITING);
      statuses.add(REJECT);
      statuses.add(BLOCKED);
    }

    if(!statuses.contains(mainServiceEntity.getStatus()))
      throw new ApplicationException(
          ErrorCode.SERVICE_CAN_NOT_VIEW, ErrorMessage.SERVICE_CAN_NOT_VIEW
      );
    FullServiceInfo serviceInfo = mapperUtils.map(mainServiceEntity, FullServiceInfo.class);
    serviceInfo.setImages(
        galleryService.findByObjectTypeAndId(GalleryObjectType.SERVICE.name(), mainServiceEntity.getId())
    );
    return serviceInfo;
  }

  private boolean isServiceOwner(Integer accountId, Integer ownerId){
    return accountId.compareTo(ownerId) == 0;
  }

  @Override
  @Transactional
  public FullServiceInfo create(Integer shopId, Integer accountId, CreateServiceRequest request) throws ApplicationException {
    ShopEntity shopEntity = findShopById(shopId);
    checkShopOwner(shopEntity.getOwnerId(), accountId);
    CityEntity cityEntity = findCityById(request.getCityId());
    CategoryEntity categoryEntity = findCategoryById(request.getCategoryId());

    MainServiceEntity mainServiceEntity = createMainService(shopEntity, cityEntity, categoryEntity, request);
    FullServiceInfo serviceInfo = mapperUtils.map(mainServiceEntity, FullServiceInfo.class);
    serviceInfo.setImages(galleryService.findByObjectTypeAndId(GalleryObjectType.SERVICE.name(), mainServiceEntity.getId()));
    return serviceInfo;
  }

  @Override
  @Transactional
  public FullServiceInfo update(Integer serviceId, Integer accountId, UpdateServiceRequest request) throws ApplicationException, DateTimeParseException{
    MainServiceEntity mainService = findEntityById(serviceId);
    checkShopOwner(mainService.getShop().getOwnerId(), accountId);

    CityEntity cityEntity = mainService.getCity();
    CategoryEntity categoryEntity = mainService.getCategory();
    if (!cityEntity.getId().equals(request.getCityId())) {
      cityEntity = findCityById(request.getCityId());
    }
    if (!categoryEntity.getId().equals(request.getCategoryId())) {
      categoryEntity = findCategoryById(request.getCategoryId());
    }

    mainService = update(mainService, cityEntity, categoryEntity, request);
    FullServiceInfo fullServiceInfo = mapperUtils.map(mainService, FullServiceInfo.class);
    List<String> images = galleryService.findByObjectTypeAndId(
        GalleryObjectType.SERVICE.name(),
        mainService.getId()
    );
    fullServiceInfo.setImages(images);
    return fullServiceInfo;
  }

  @Override
  public void delete(Integer serviceId, Integer accountId) throws ApplicationException {
    MainServiceEntity mainServiceEntity = findEntityById(serviceId);
    checkShopOwner(mainServiceEntity.getShop().getOwnerId(), accountId);
    updateStatus(mainServiceEntity, ServiceStatus.DELETED);
  }

  public ServiceStatus getServiceStatus(String status){
    if (status.equalsIgnoreCase(WAITING.name()))
      return WAITING;
    else if (status.equalsIgnoreCase(DELETED.name()))
      return DELETED;
    else if(status.equalsIgnoreCase(UNAVAILABLE.name()))
      return UNAVAILABLE;
    else if (status.equalsIgnoreCase(ACTIVE.name()))
      return ACTIVE;
    else return null;
  }

  @Override
  public void updateStatus(Integer serviceId, ServiceStatus status, Integer accountId) throws ApplicationException {
    MainServiceEntity mainServiceEntity = findEntityById(serviceId);
    mainServiceEntity.setStatus(status);
    mainRepository.save(mainServiceEntity);

    CreateNotificationRequestPayload payload = CreateNotificationRequestPayload.builder()
        .receiverId(Long.valueOf(mainServiceEntity.getShop().getOwnerId()))
        .title(NOTIFY_SERVICE_STATUS_CHANGE_TITLE)
        .message(String.format(NOTIFY_SERVICE_STATUS_CHANGE_MESSAGE, mainServiceEntity.getShop().getId(), status.name(), accountId))
        .createdAt(ZonedDateTime.now())
        .build();
    messagingService.sendNewNotificationRequestToQueue(payload);
  }

  @Override
  public List<SubServiceEntity> findByIdIn(List<Integer> ids) {
    return subRepository.findByIdIn(ids);
  }

  @Override
  public Page<MainServiceEntity> findByModifiedDate(Integer page, Integer size, String sortType, List<ServiceStatus> statuses) {
    if (sortType.equalsIgnoreCase("desc"))
      return mainRepository.findByStatusIn(statuses, PageRequest.of(page-1, size, Sort.by("modifiedAt").descending()));
    else
      return mainRepository.findByStatusIn(statuses, PageRequest.of(page-1, size, Sort.by("modifiedAt")));
  }

  @Override
  public void updateRate(UpdateRateRequest request) throws ApplicationException {
    MainServiceEntity mainServiceEntity = findEntityById(request.getMainServiceId());
    mainServiceEntity.setRateAverage(request.getRateAverage());
    mainServiceEntity.setRateCount(request.getRateCount());
    mainRepository.save(mainServiceEntity);
  }

  public Long getLowestPrice(Integer mainServiceId) {
    List<SubServiceEntity> subServiceEntities = subRepository.findByMainServiceIdOrderByPriceDesc(mainServiceId);
    if(subServiceEntities.size()>0)
      return subServiceEntities.get(0).getPrice();
    return 0L;
  }

  @Override
  public Page<MainServiceEntity> findByCategoryAndStatusIn(Integer categoryId, Integer page, Integer size, List<ServiceStatus> statuses) {
    return mainRepository.findByCategoryIdAndStatusIn(categoryId, statuses, PageRequest.of(page-1, size));
  }

  @Override
  public Page<MainServiceEntity> findByStatusAndRateAverage(ServiceStatus status, Float rateAverage, Integer page, Integer size) {
    return mainRepository.findByStatusAndRateAverageGreaterThanEqual(status, rateAverage, PageRequest.of(page-1, size));
  }

  @Override
  public List<MainServiceEntity> filter(FilterRequest request) {
    return mainRepository.filter(request);
  }

  @Override
  public PaginationServiceInfo findServiceInfoByShopIdAndStatus(Integer shopId, ServiceStatus status, Integer page, Integer size) {
    Page<MainServiceEntity> pages = mainRepository.findByShop_IdAndStatus(shopId, status, PageRequest.of(page-1, size));
    return toPagination(pages, page, size);
  }


  @Override
  public PaginationServiceInfo findByNameAndStatusIn(String name, List<ServiceStatus> statusList, Integer page, Integer size)  {
    Page<MainServiceEntity> rs = mainRepository.findByTitleContainsAndStatusIn(name, statusList, PageRequest.of(page-1, size));
    return toPagination(rs, page, size);
  }

   @Override
   public ListVerifyResponse getListVerifyResponse(Integer page, Integer size) {
       Page<MainServiceEntity> pages = mainRepository.findByStatus(WAITING, PageRequest.of(page-1, size));
       List<FullServiceInfo> serviceInfos = mapperUtils.mapToFullServiceInfo(pages.getContent());
       return ListVerifyResponse.builder()
               .page(page)
               .size(size)
               .totalPage(pages.getTotalPages())
               .totalElement(pages.getTotalElements())
               .services(serviceInfos)
           .build();
   }

   @Override
   public void verify(UpdateVerifyStatusRequest request, Integer accountId) throws ApplicationException {

       MainServiceEntity mainServiceEntity = findEntityById(request.getServiceId());
       if(mainServiceEntity.getStatus() == WAITING){
          mainServiceEntity.setStatus(request.getStatus());
          mainRepository.save(mainServiceEntity);
          // notify: handle late
         CreateNotificationRequestPayload payload = CreateNotificationRequestPayload.builder()
             .receiverId(Long.valueOf(mainServiceEntity.getShop().getOwnerId()))
             .title(NOTIFY_SHOP_STATUS_CHANGE_TITLE)
             .message(String.format(NOTIFY_SHOP_STATUS_CHANGE_MESSAGE, mainServiceEntity.getShop().getId(), ACTIVE.name(), accountId))
             .createdAt(ZonedDateTime.now())
             .build();
         messagingService.sendNewNotificationRequestToQueue(payload);
       }
   }
}

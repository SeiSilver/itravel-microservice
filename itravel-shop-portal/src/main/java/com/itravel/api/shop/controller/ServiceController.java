package com.itravel.api.shop.controller;

import com.itravel.api.shop.annotation.CurrentUser;
import com.itravel.api.shop.config.ApplicationProperties;
import com.itravel.api.shop.constraint.DefaultValue;
import com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint;
import com.itravel.api.shop.enums.GalleryObjectType;
import com.itravel.api.shop.enums.ServiceStatus;
import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.ApplicationException;
import com.itravel.api.shop.model.dto.FullServiceInfo;
import com.itravel.api.shop.model.dto.MainServiceInfo;
import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.payload.FilterRequest;
import com.itravel.api.shop.payload.PaginationServiceInfo;
import com.itravel.api.shop.payload.ServiceFrequency;
import com.itravel.api.shop.payload.ServiceFrequencyResponse;
import com.itravel.api.shop.security.principal.AccountPrincipal;
import com.itravel.api.shop.service.GalleryService;
import com.itravel.api.shop.service.ITravelService;
import com.itravel.api.shop.util.ModelMapperUtils;
import com.itravel.api.shop.util.PayloadUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.itravel.api.shop.constraint.DefaultValue.SORT_TYPE_DESC;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.CATEGORY;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.FILTER;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.POPULAR;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.RANDOM;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SERVICE_ID;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SHOP_SHOP_ID;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.SEARCH;
import static com.itravel.api.shop.controller.endpoint.ShopPortalEndpoint.NEW;
import static com.itravel.api.shop.constraint.DefaultValue.SIZE;
import static com.itravel.api.shop.constraint.DefaultValue.PAGE;


@RequiredArgsConstructor
@RequestMapping(ShopPortalEndpoint.SERVICE)
@RestController
@Log4j2
public class ServiceController {
    private final ITravelService iTravelService;
    private final GalleryService galleryService;
    private final ModelMapperUtils mapperUtils;
    private final RestTemplate restTemplate;
    private final ApplicationProperties properties;
    private final PayloadUtils payloadUtils;
    private final ServiceStatus[] baseServiceStatusArr = {ServiceStatus.ACTIVE, ServiceStatus.UNAVAILABLE};
    private final List<ServiceStatus> baseServiceStatus = new ArrayList<>(Arrays.asList(baseServiceStatusArr));


    @GetMapping
    @ApiOperation(value = "Get all service. Pagination support")
    public ResponseEntity<PaginationServiceInfo> findAll(
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size) throws ApplicationException {
        log.info("Receive find all service by id request with page: {} and size: {}", page, size);
        return ResponseEntity.ok(iTravelService.findAll(page, size, baseServiceStatus));
    }

    @GetMapping(SERVICE_ID)
    @ApiOperation(value = "Find service by serviceId")
    public ResponseEntity<FullServiceInfo> findById(
        @PathVariable Integer serviceId,
        @ApiIgnore @CurrentUser AccountPrincipal principal) throws ApplicationException {
        log.info("Receive find service by id request with payload: {}", serviceId);
        Integer accountId = principal == null ? 0 : Math.toIntExact(principal.getAccountVerified().getId());
        return ResponseEntity.ok(iTravelService.findServiceInfoById(accountId, serviceId, baseServiceStatus));
    }

    @GetMapping(SHOP_SHOP_ID)
    @ApiOperation(value = "Get shop service info by service id")
    public ResponseEntity<PaginationServiceInfo> getShopServiceByShopId(
        @PathVariable Integer shopId, @RequestParam(name = "page", required = false, defaultValue = DefaultValue.PAGE) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = DefaultValue.SIZE) Integer size
    )throws ApplicationException {
        log.info("Receive get service by shopId: {}", shopId);
        return ResponseEntity.ok(iTravelService.findServiceInfoByShopIdAndStatus(shopId, ServiceStatus.ACTIVE, page, size));
    }

    @GetMapping(SEARCH)
    @ApiOperation(value = "Find service by name. Support pagination: page vs size(default 20)")
    public ResponseEntity<PaginationServiceInfo> findByName(
        @RequestParam String name, @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size) throws ApplicationException {
        return ResponseEntity.ok(iTravelService.findByNameAndStatusIn(name, List.of(ServiceStatus.ACTIVE), page, size));
    }

    @PostMapping(FILTER)
    @ApiOperation(value = "Filter service. Pagination support. Attribute list: mainServiceName")
    public ResponseEntity<PaginationServiceInfo> filter(
        @RequestBody FilterRequest request
        ){
        List<MainServiceEntity> rs = iTravelService.filter(request);
        return ResponseEntity.ok(
            PaginationServiceInfo.builder()
                .page(request.getPage())
                .size(request.getSize())
                .serviceInfos(toMainServiceInfos(rs))
                .build()
        );
    }

    @GetMapping(NEW)
    @ApiOperation(value = "Get new service. Pagination support")
    public ResponseEntity<PaginationServiceInfo> findNewService(
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size,
        @RequestParam(required = false, defaultValue = SORT_TYPE_DESC) String sortType
    ){
        log.info("Receive get new service with page {} - size {}", page, size);
        Page<MainServiceEntity> pages = iTravelService.findByModifiedDate(page, size, sortType, baseServiceStatus);
        List<MainServiceInfo> mainServiceInfos = new ArrayList<>();
        for(MainServiceEntity mainService : pages.getContent()){
            MainServiceInfo mainServiceInfo = mapperUtils.map(mainService, MainServiceInfo.class);
            mainServiceInfo.setImages(galleryService.findByObjectTypeAndId(GalleryObjectType.SERVICE.name(), mainService.getId()));
            mainServiceInfos.add(mainServiceInfo);
        }
        return ResponseEntity.ok(
            PaginationServiceInfo.builder()
                .page(page)
                .size(size)
                .totalPage(pages.getTotalPages())
                .totalItem(pages.getTotalElements())
                .serviceInfos(mainServiceInfos)
                .build()
        );
    }

    @GetMapping(POPULAR)
    @ApiOperation(value = "Get popular service")
    public ResponseEntity<PaginationServiceInfo> findPopularService() throws ApplicationException {
        log.info("Receive get popular service");
        ServiceFrequencyResponse response = restTemplate.getForObject(properties.getPaymentPortal().getServiceFrequency(), ServiceFrequencyResponse.class);
        if(response == null)
            throw new ApplicationException(
                ErrorCode.GET_SERVICE_FREQUENCY_FAIL, ErrorMessage.GET_SERVICE_FREQUENCY_FAIL
            );
        else{
            List<MainServiceInfo> mainServiceInfos = new ArrayList<>();
            for(ServiceFrequency serviceFrequency : response.getServiceFrequencies()){
                Optional<MainServiceEntity> optionalMainService = iTravelService.findOptionalById(serviceFrequency.getMainServiceId());
                if(optionalMainService.isPresent() && optionalMainService.get().getStatus() == ServiceStatus.ACTIVE){
                    MainServiceInfo mainServiceInfo = mapperUtils.map(optionalMainService.get(), MainServiceInfo.class);
                    mainServiceInfo.setImages(galleryService.findByObjectTypeAndId(GalleryObjectType.SERVICE.name(), mainServiceInfo.getId()));
                    mainServiceInfos.add(mainServiceInfo);
                }
            }
            return ResponseEntity.ok(
                PaginationServiceInfo.builder()
                    .page(1)
                    .size(mainServiceInfos.size())
                    .totalPage(1)
                    .totalItem((long) mainServiceInfos.size())
                    .serviceInfos(mainServiceInfos)
                    .build()
            );
        }
    }

    @GetMapping(CATEGORY)
    @ApiOperation(value = "Find service by category id. Pagination support")
    public ResponseEntity<PaginationServiceInfo> findByCategory(
        @RequestParam(required = false, defaultValue = PAGE) Integer page,
        @RequestParam(required = false, defaultValue = SIZE) Integer size,
        @RequestParam(required = false, defaultValue = "1") Integer categoryId
    ){
        log.info("Receive get service by category id {}", categoryId);
        Page<MainServiceEntity> pages = iTravelService.findByCategoryAndStatusIn(categoryId, page, size, baseServiceStatus);
        List<MainServiceInfo> mainServiceInfos = toMainServiceInfos(pages.getContent());

        return ResponseEntity.ok(
            PaginationServiceInfo.builder()
                .size(size)
                .page(page)
                .totalPage(pages.getTotalPages())
                .totalItem(pages.getTotalElements())
                .serviceInfos(mainServiceInfos)
                .build()
        );
    }

    @GetMapping(RANDOM)
    public ResponseEntity<PaginationServiceInfo> findRandomService(
        @RequestParam(required = false, defaultValue = SIZE) Integer size
    ){
        log.info("Receive get random service info with size {}", size);
        Page<MainServiceEntity> temp = iTravelService.findByStatusAndRateAverage(ServiceStatus.ACTIVE, 3.5f, 1, 1);
        int maxPage = 1+ temp.getNumberOfElements()/size;
        Integer page = new Random().nextInt(maxPage)+1;
        Page<MainServiceEntity> pages = iTravelService.findByStatusAndRateAverage(ServiceStatus.ACTIVE, 3.5f, page, size);
        List<MainServiceInfo> mainServiceInfos = toMainServiceInfos(pages.getContent());
        return ResponseEntity.ok(
            PaginationServiceInfo.builder()
                .size(size)
                .page(page)
                .totalPage(pages.getTotalPages())
                .totalItem(pages.getTotalElements())
                .serviceInfos(mainServiceInfos)
                .build()
        );
    }

    private List<MainServiceInfo> toMainServiceInfos(List<MainServiceEntity> list){
        List<MainServiceInfo> mainServiceInfos = new ArrayList<>();
        for(MainServiceEntity mainService : list){
            if(baseServiceStatus.contains(mainService.getStatus())) {
                MainServiceInfo mainServiceInfo = mapperUtils.map(mainService, MainServiceInfo.class);
                mainServiceInfo.setImages(galleryService.findByObjectTypeAndId(GalleryObjectType.SERVICE.name(), mainService.getId()));
                mainServiceInfos.add(mainServiceInfo);
            }
        }
        return mainServiceInfos;
    }
}

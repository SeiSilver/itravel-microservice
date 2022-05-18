package com.itravel.api.payment.service.impl;

import com.itravel.api.payment.config.ApplicationProperties;
import com.itravel.api.payment.constraint.ErrorCode;
import com.itravel.api.payment.constraint.ErrorMessage;
import com.itravel.api.payment.enums.OrderStatus;
import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.dto.AccountBasicInfo;
import com.itravel.api.payment.model.entity.OrderEntity;
import com.itravel.api.payment.model.entity.OrderItemEntity;
import com.itravel.api.payment.model.jpa.OrderItemRepository;
import com.itravel.api.payment.model.jpa.OrderRepository;
import com.itravel.api.payment.payload.CreateOrderRequest;
import com.itravel.api.payment.payload.EmailOutGoingRequest;
import com.itravel.api.payment.payload.GetAccountsByIdsPayload;
import com.itravel.api.payment.payload.SellerOrder;
import com.itravel.api.payment.payload.ServiceFrequency;
import com.itravel.api.payment.payload.TotalOrderItemPrice;
import com.itravel.api.payment.service.AccountService;
import com.itravel.api.payment.service.EmailService;
import com.itravel.api.payment.service.OrderService;
import com.itravel.api.payment.util.CommonUtils;
import com.itravel.api.payment.util.ModelMapperUtils;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ModelMapperUtils modelMapperUtils;
  private final EmailService emailService;
  private final AccountService accountService;
  private final ApplicationProperties applicationProperties;

  @Override
  public OrderEntity getOrderStatusByOrderBillId(String orderBillId) throws ApplicationException {
    return orderRepository.findByOrderBillId(orderBillId).orElseThrow(
        () -> new ApplicationException(41, "Order not found!")
    );
  }

  @Override
  @Transactional
  public OrderEntity updateOrderStatusByOrderBillId(String orderBillId, OrderStatus orderStatus) throws ApplicationException {
    var orderEntity = orderRepository.findByOrderBillId(orderBillId).orElseThrow(
        () -> new ApplicationException(41, "Order not found!")
    );
    orderEntity.setStatus(orderStatus);
    return orderRepository.save(orderEntity);
  }

  @Override
  @Transactional
  public void createOrder(CreateOrderRequest createOrderRequest) {
    OrderEntity order = modelMapperUtils.map(createOrderRequest.getOrder(), OrderEntity.class);
    order.setId(0);
    order.setStatus(OrderStatus.NEW);
    order.setCreatedAt(ZonedDateTime.now());
    var orderEntity = orderRepository.save(order);

    var listOrderItems = modelMapperUtils.mapList(createOrderRequest.getOrderItems(), OrderItemEntity.class);
    listOrderItems.forEach(i -> {
          i.setId(0);
          i.setCreatedAt(ZonedDateTime.now());
          i.setOrder(orderEntity);
        }
    );
    orderItemRepository.saveAll(listOrderItems);
  }

  @Override
  public void sendEmail(String orderBillId) throws MessagingException, ApplicationException {
    OrderEntity orderEntity = getOrderStatusByOrderBillId(orderBillId);
    GetAccountsByIdsPayload payload = GetAccountsByIdsPayload.builder()
        .listId(List.of(orderEntity.getAccountId()))
        .build();
    AccountBasicInfo accountBasicInfo = accountService.getAccountByAccountIds(payload).get(0);

    log.info("START... Sending email");
    EmailOutGoingRequest mail = new EmailOutGoingRequest();
    mail.setMailTo(accountBasicInfo.getEmail());
    mail.setSubject("Cảm ơn đã sử dụng dịch vụ iTravel!");
    Map<String, Object> model = new HashMap<>();
    model.put("name", accountBasicInfo.getFullName());
    model.put("createdAt", CommonUtils.convertToDate(orderEntity.getCreatedAt()));
    String orderLink = applicationProperties.getUx().getBaseUrl() + "/orderdetail/"
        + orderEntity.getId() + "?code=" + orderBillId;
    model.put("orderLink", orderLink);
    model.put("fullName", orderEntity.getFullName());
    model.put("phoneNumber", orderEntity.getPhoneNumber());
    mail.setProps(model);
    emailService.sendPaymentOrderEmail(mail, "email-template");
    log.info("END... Email sent success");
  }

  @Override
  public Page<OrderEntity> findByAccountId(Long accountId, Integer page, Integer size) {
    return orderRepository.findByAccountId(accountId, PageRequest.of(page - 1, size));
  }

  @Override
  public OrderEntity findById(Integer id) throws ApplicationException {
    return orderRepository.findById(id).orElseThrow(
        () -> new ApplicationException(
            ErrorCode.ORDER_NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND
        )
    );
  }

  @Override
  public Boolean isOrderOwner(Integer accountId, Integer ownerId) {
    return accountId.compareTo(ownerId) == 0;
  }

  @Override
  public Page<OrderEntity> findAllByStatusIn(Integer page, Integer size, List<OrderStatus> statuses) {
    if (statuses.isEmpty()) {
      return orderRepository.findAll(PageRequest.of(page - 1, size));
    } else {
      return orderRepository.findByStatusIn(statuses, PageRequest.of(page - 1, size));
    }
  }

  @Override
  public List<ServiceFrequency> getServiceFrequency() {
    return orderItemRepository.findByServiceCount(PageRequest.of(0, 10));
  }


  @Override
  public Page<SellerOrder> findSellerOrderByShopId(Integer shopId, Integer page, Integer size) {
    return orderRepository.findSellerOrderByShopId(shopId, PageRequest.of(page-1, size));
  }

  @Override
  public List<TotalOrderItemPrice> getTotalPriceByOrderIdAndShopId(Integer orderId, Integer shopId) {
    return orderItemRepository.getTotalPriceByOrderIdAndShopId(orderId, shopId);
  }

  @Override
  public Page<OrderItemEntity> findByShopIdAndOrderId(Integer shopId, Integer orderId, Integer page, Integer size) {
    return orderItemRepository.findByShopIdAndOrderId(shopId, orderId, PageRequest.of(page-1, size));
  }
}

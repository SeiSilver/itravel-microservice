package com.itravel.api.payment.service;

import com.itravel.api.payment.enums.OrderStatus;
import com.itravel.api.payment.exception.ApplicationException;
import com.itravel.api.payment.model.entity.OrderEntity;
import com.itravel.api.payment.model.entity.OrderItemEntity;
import com.itravel.api.payment.payload.CreateOrderRequest;
import com.itravel.api.payment.payload.SellerOrder;
import com.itravel.api.payment.payload.ServiceFrequency;
import com.itravel.api.payment.payload.TotalOrderItemPrice;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.util.List;

public interface OrderService {

  OrderEntity getOrderStatusByOrderBillId(String orderBillId) throws ApplicationException;

  OrderEntity updateOrderStatusByOrderBillId(String orderBillId, OrderStatus orderStatus) throws ApplicationException;

  void createOrder(CreateOrderRequest createOrderRequest);

  void sendEmail(String orderBillId) throws MessagingException, ApplicationException;

  Page<OrderEntity> findByAccountId(Long accountId, Integer page, Integer size);

  OrderEntity findById(Integer id) throws ApplicationException;

  Boolean isOrderOwner(Integer accountId, Integer ownerId);

  Page<OrderEntity> findAllByStatusIn(Integer page, Integer size, List<OrderStatus> statuses);

  List<ServiceFrequency> getServiceFrequency();

  Page<SellerOrder> findSellerOrderByShopId(Integer shopId, Integer page, Integer size);

  List<TotalOrderItemPrice> getTotalPriceByOrderIdAndShopId(Integer orderId, Integer shopId);

  Page<OrderItemEntity> findByShopIdAndOrderId(Integer shopId, Integer orderId, Integer page, Integer size);
}

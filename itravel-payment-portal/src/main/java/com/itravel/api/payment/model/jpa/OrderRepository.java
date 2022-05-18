package com.itravel.api.payment.model.jpa;

import com.itravel.api.payment.enums.OrderStatus;
import com.itravel.api.payment.model.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

import com.itravel.api.payment.payload.SellerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

  Optional<OrderEntity> findByOrderBillId(String orderBillId);

  Page<OrderEntity> findByAccountId(Long accountId, Pageable pageable);

  Page<OrderEntity> findByStatusIn(List<OrderStatus> statuses, Pageable pageable);

  @Query("select new com.itravel.api.payment.payload.SellerOrder(o.id, o.orderBillId, o.fullName, o.phoneNumber, o.status, o.createdAt ) from OrderEntity o inner join OrderItemEntity oi on o.id = oi.order.id where oi.shopId = :shopId")
  Page<SellerOrder> findSellerOrderByShopId(Integer shopId, Pageable pageable);
}

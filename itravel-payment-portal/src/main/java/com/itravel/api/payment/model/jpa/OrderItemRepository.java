package com.itravel.api.payment.model.jpa;

import com.itravel.api.payment.model.entity.OrderItemEntity;
import com.itravel.api.payment.payload.ServiceFrequency;
import com.itravel.api.payment.payload.TotalOrderItemPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {

    @Query("SELECT new com.itravel.api.payment.payload.ServiceFrequency(ot.mainServiceId, sum(ot.mainServiceId) ) from OrderItemEntity ot group by ot.mainServiceId")
    List<ServiceFrequency> findByServiceCount(Pageable pageable);

    Page<OrderItemEntity> findByShopId(Integer shopId, Pageable pageable);

    @Query("select new com.itravel.api.payment.payload.TotalOrderItemPrice(oi.price * oi.quantity) from OrderItemEntity  oi where oi.order.id = :orderId and oi.shopId = :shopId")
    List<TotalOrderItemPrice> getTotalPriceByOrderIdAndShopId(Integer orderId, Integer shopId);

    Page<OrderItemEntity> findByShopIdAndOrderId(Integer shopId, Integer orderId, Pageable pageable);
}

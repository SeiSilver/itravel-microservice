package com.itravel.api.payment.model.jpa;

import com.itravel.api.payment.model.entity.RefundRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequestEntity, Integer> {

  Page<RefundRequestEntity> findAllByAccountId(Pageable pageable, Long accountId);

  @Query("SELECT r FROM RefundRequestEntity r "
      + "where r.orderItem.order.orderBillId like %?1%")
  Page<RefundRequestEntity> findAllByOrderBillId(Pageable pageable, String orderBillId);

}

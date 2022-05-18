package com.itravel.api.payment.model.entity;

import com.itravel.api.payment.enums.OrderStatus;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "order")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String orderBillId;

  @Column(name = "account_id", nullable = false)
  private Long accountId;
  private String fullName;
  private String phoneNumber;
  private Integer totalPrice;

  @OneToMany(mappedBy = "order")
  private List<OrderItemEntity> orderItems;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @CreatedDate
  private ZonedDateTime createdAt;

}

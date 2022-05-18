package com.itravel.api.payment.model.entity;

import com.itravel.api.payment.enums.RefundStatus;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "refund_request")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RefundRequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Long accountId;

  private String reportType;

  private String requestReason;

  private String refuseReason;

  @Enumerated(EnumType.STRING)
  private RefundStatus status;

  @CreatedDate
  private ZonedDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "order_item_id", referencedColumnName = "id", nullable = false)
  private OrderItemEntity orderItem;

}

package com.itravel.api.payment.model.entity;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
@Table(name = "order_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer subServiceId;

  private Integer mainServiceId;

  private Integer shopId;

  private String shopName;

  private String mainServiceTitle;

  private String subServiceTitle;

  private String image;

  private Integer quantity;

  private Integer price;

  private ZonedDateTime usedStart;

  private ZonedDateTime usedEnd;

  @CreatedDate
  private ZonedDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  private OrderEntity order;

}

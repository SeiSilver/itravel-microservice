package com.itravel.api.shop.model.entity;

import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itravel.api.shop.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "sub_service")
@EntityListeners(AuditingEntityListener.class)
public class SubServiceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String title;

  private Long price;

  private Integer stockAmount;


  @Enumerated(EnumType.STRING)
  private ServiceStatus status;

  @ManyToOne
  @JoinColumn(name = "main_service_id")
  private MainServiceEntity mainService;

  @LastModifiedDate
  private ZonedDateTime modifiedAt;

  @CreatedDate
  private ZonedDateTime createdAt;

  @JsonIgnore
  @OneToMany(mappedBy = "subService")
  private List<CartItemEntity> cartItems;
}

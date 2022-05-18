package com.itravel.api.shop.model.entity;

import com.itravel.api.shop.enums.ServiceStatus;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "main_service")
@EntityListeners(AuditingEntityListener.class)
public class MainServiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String title;

  private String description;

  private Integer duration;

  @ManyToOne
  @JoinColumn(name = "shop_id", nullable = false)
  private ShopEntity shop;

  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private CityEntity city;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @OneToMany(mappedBy = "mainService")
  private List<SubServiceEntity> subServices;

  private String address;

  private Long rateCount;

  private Float rateAverage;

  @Enumerated(EnumType.STRING)
  private ServiceStatus status;

  private ZonedDateTime eventStart;

  private ZonedDateTime eventEnd;

  private Long lowestPrice;

  @LastModifiedDate
  private ZonedDateTime modifiedAt;

  @CreatedDate
  private ZonedDateTime createdAt;
}

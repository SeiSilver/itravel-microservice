package com.itravel.api.shop.model.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "cart_item")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantity;
    @CreatedDate
    private ZonedDateTime createdAt;
    private ZonedDateTime usedStart;
    private ZonedDateTime usedEnd;
    private Integer accountId;
    @ManyToOne
    @JoinColumn(name = "sub_service_id")
    private SubServiceEntity subService;
}

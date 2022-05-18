package com.itravel.api.payment.payload;

import com.itravel.api.payment.model.dto.OrderSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaginationOrderSummary {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalItem;
    private List<OrderSummary> results;
}

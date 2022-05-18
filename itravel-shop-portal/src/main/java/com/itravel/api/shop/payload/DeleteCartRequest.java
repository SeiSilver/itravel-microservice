package com.itravel.api.shop.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeleteCartRequest {
    @NotNull
    private List<Integer> cartIds;
}

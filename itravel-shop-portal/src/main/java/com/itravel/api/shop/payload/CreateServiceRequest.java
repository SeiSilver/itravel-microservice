package com.itravel.api.shop.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateServiceRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String address;
    @NotNull
    private Integer cityId;
    @NotNull
    private Integer categoryId;
    @NotNull
    private Integer duration;
    @NotEmpty
    private String eventStart;
    @NotEmpty
    private String eventEnd;
    private List<String> images;
    @NotNull
    @Valid
    private List<CreateSubServiceRequest> subServices;
}

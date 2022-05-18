package com.itravel.api.shop.controller.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShopPortalEndpoint {
    public static final String SHOP = "/shop";
    public static final String SHOP_ID = "/{shopId}";
    public static final String SHOP_ID_SERVICE = "/{shopId}/service";
    public static final String SERVICE_SERVICE_ID = "/service/{serviceId}";
    public static final String SERVICE = "/service";
    public static final String SERVICE_ID = "/{serviceId}";
    public static final String SEARCH = "/search";
    public static final String FILTER = "/filter";
    public static final String SHOP_SHOP_ID = "/shop/{shopId}";
    public static final String CART = "/cart";
    public static final String CART_ITEM_ID = "/{cartItemId}";
    public static final String VERIFY = "/verify";
    public static final String ORDER ="/order";
    public static final String MOD ="/mod";
    public static final String NAME_SHOP_NAME ="/name/{shopName}";
    public static final String SHOP_STATUS = "shop/status";
    public static final String GALLERY = "/gallery";
    public static final String NEW = "/new";
    public static final String POPULAR ="/popular";
    public static final String CATEGORY = "/category";
    public static final String CATEGORY_ID = "/{categoryId}";
    public static final String OWNER = "/owner";
    public static final String RANDOM ="/random";
}

package com.itravel.api.shop.error;

public class ErrorCode {

    public static final int MISSING_REQUIRED_FIELD = 5;
    public static final int AUTHORIZED_FAIL = 6;
    public static final int ACCESS_DENIED = 7;
    public static final int MAIN_SERVICE_NOT_FOUND = 10;
    public static final int SUB_SERVICE_NOT_FOUND = 11;
    public static final int INVALID_JSON_PAYLOAD = 20;
    public static final int TOKEN_INVALID = 15;
    public static final int TOKEN_EXPIRED = 16;
    public static final int ACCOUNT_LOCKED = 22;
    public static final int FILE_INVALID = 24;
    public static final int SHOP_NOT_FOUND = 25;
    public static final int CITY_NOT_FOUND = 26;
    public static final int CATEGORY_NOT_FOUND = 27;
    public static final int SERVICE_NOT_OWNED_BY_SHOP = 28;
    public static final int NOT_SHOP_OWNER = 29;
    public static final int NOT_SERVICE_OWNER = 30;
    public static final int CART_ITEM_NOT_FOUND = 31;
    public static final int NOT_CART_OWNER = 32;
    public static final int GALLERY_NOT_FOUND = 33;
    public static final int SHOP_CAN_NOT_PROCESS = 34;
    public static final int SERVICE_CAN_NOT_PROCESS = 35;
    public static final int INVALID_USED_DATE = 36;
    public static final int MISSING_CART_ITEM = 37;
    public static final int NOT_ENOUGH_QUANTITY = 38;
    public static final int NOT_HAVE_SHOP = 39;
    public static final int SERVICE_PRICE_INVALID = 40;
    public static final int SUB_SERVICE_CAN_NOT_PROCESS = 41;
    public static final int GET_SERVICE_FREQUENCY_FAIL=43;
    public static final int SERVICE_CAN_NOT_VIEW = 44;
    public static final int SHOP_EXIST = 45;
    public static final int REGISTER_FAIL = 46;
    public static final int UNKNOWN_ERROR = 99;
}

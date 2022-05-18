package com.itravel.api.shop.error;

public class ErrorMessage {
    public static final String AUTHORIZED_FAIL = "Authentication Failed, please try again!";
    public static final String INVALID_JSON_PAYLOAD = "Payload parse exception";
    public static final String SHOP_NOT_FOUND = "Shop not found";
    public static final String CITY_NOT_FOUND = "City not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String MAIN_SERVICE_NOT_FOUND = "Main service not found";
    public static final String SUB_SERVICE_NOT_FOUND = "Sub service not found";
    public static final String SERVICE_NOT_OWNED_BY_SHOP = "Service not owned by shop";
    public static final String NOT_SERVICE_OWNER = "Account is not service owner";
    public static final String NOT_SHOP_OWNER = "Account is not shop owner";
    public static final String TOKEN_INVALID = "Invalid token!";
    public static final String TOKEN_EXPIRED = "Token expired!";
    public static final String ACCESS_DENIED = "Access denied!";
    public static final String FILE_UPLOAD_FAIL = "File upload fail!";
    public static final String FILE_TYPE_INVALID = "This file extension is invalid!";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found";
    public static final String NOT_CART_OWNER = "Account is not cart owner";
    public static final String GALLERY_NOT_FOUND = "Gallery not found";
    public static final String SHOP_CAN_NOT_PROCESS = "Shop %s can not process request because status %s";
    public static final String SERVICE_CAN_NOT_PROCESS = "Service %s can not process request because status %s";
    public static final String SUB_SERVICE_CAN_NOT_PROCESS = "Sub service %s can not process request because status %s";
    public static final String ACCOUNT_LOCKED = "Account is locked!";
    public static final String INVALID_USED_DATE = "Used date %s is not in event scope from %s - %s";
    public static final String MISSING_CART_ITEM = "Cart item list is empty";
    public static final String NOT_ENOUGH_QUANTITY = "Not enough quantity required. Stock amount: %s - Request: %s";
    public static final String NOT_HAVE_SHOP = "Account not have any shop";
    public static final String SERVICE_PRICE_INVALID = "Service's price much higher or equal 10000 VND. Current is %s";
    public static final String GET_SERVICE_FREQUENCY_FAIL ="Get service frequency fail";
    public static final String SERVICE_CAN_NOT_VIEW = "Service can not be view now";
    public static final String SHOP_EXIST = "Shop exist";
    public static final String  REGISTER_FAIL = "Register seller fail";
}

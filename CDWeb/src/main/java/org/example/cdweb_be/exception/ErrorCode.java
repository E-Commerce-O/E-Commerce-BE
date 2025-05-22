package org.example.cdweb_be.exception;

import lombok.Getter;
import org.example.cdweb_be.enums.OrderStatus;
import org.example.cdweb_be.enums.VoucherType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter

public enum ErrorCode {
    UNAUTHENTICATED(4001, "Unauthenticated!", HttpStatus.UNAUTHORIZED),//401,
    TAG_EXISTED(4000, "Tag name existed!", HttpStatus.BAD_REQUEST),
    IMAGE_REQUIRED(4002, "Image file is required!", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(4003, "Category name existed!", HttpStatus.BAD_REQUEST),
    NOT_FOUND(4004, "Not found!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTS(4005, "CategoryId not exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(4006, "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(4007, "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(4008, "Email invalid!", HttpStatus.BAD_REQUEST),
    PHONENUMER_INVALID(4009, "Phone number invalid!", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(4010, "Username existed!", HttpStatus.BAD_REQUEST), //400
    EMAIL_EXISTED(4011, "Email existed!", HttpStatus.BAD_REQUEST), //400
    PHONE_NUMBER_EXISTED(4012, "Phone number existed!", HttpStatus.BAD_REQUEST), //400
    USER_NOT_EXISTS(4013, "Username not exists!", HttpStatus.BAD_REQUEST),//400
    PASSWORD_INCORRECT(4014, "Incorrect password!", HttpStatus.BAD_REQUEST),//400
    REFRESH_TOKEN_NOT_FOUND(4015, "Incorrect refresh token!", HttpStatus.BAD_REQUEST),//400
    REFRESH_TOKEN_INVALID(4015, "Invalid refresh token!", HttpStatus.BAD_REQUEST),//400
    ACCESS_TOKEN_INVALID(4016, "Missing accessToken or invalid accessToken!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_NOT_EXISTS(4017, "ProductId not exists!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_DETAIL_NOT_EXISTS(4018, "ProductDetailId not exists!", HttpStatus.BAD_REQUEST),//400
    IMAGE_NOT_EXISTS(4019, "ImageId not exists!", HttpStatus.BAD_REQUEST),//400
    IMAGE_INVALID(4020, "ImageId invalid with ProductId!", HttpStatus.BAD_REQUEST),//400
    ADD_TAG_FAILD(4021, "TagNames empty or all of new tag existed with ProductId!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_TAG_NOT_EXISTS(4022, "TagNames empty or all of new tag existed with ProductId!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_TAG_EMPTY(4023, "TagNames is empty!", HttpStatus.BAD_REQUEST),//400
    IMAGE_PAHTS_EMPTY(4024, "ImagePaths is empty!", HttpStatus.BAD_REQUEST),//400
    PROVINCE_NOT_EXISTS(4025, "ProvinceId not exists!", HttpStatus.BAD_REQUEST),//400
    DISTRICT_NOT_EXISTS(4026, "DistrictId not exists!", HttpStatus.BAD_REQUEST),//400
    WARD_NOT_EXISTS(4027, "WardId not exists!", HttpStatus.BAD_REQUEST),//400
    ADDRESS_EXISTED(4028, "Address already exists!", HttpStatus.BAD_REQUEST),//400
    ADDRESS_NOT_EXISTS(4029, "AddressId not exists!", HttpStatus.BAD_REQUEST),//400
    ADDRESS_UNAUTHORIZED(4030, "You do not have permission to operate on this addressId because this address not yours!", HttpStatus.BAD_REQUEST),//400
    WHISTLIST_EXISTED(4031, "ProductId already exists in your wishlist!", HttpStatus.BAD_REQUEST),//400
    WHISTLIST_NOT_EXISTS(4032, "ProductId not in your wishlist!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_SIZE_NOT_EXISTS(4033, "SizeId not exists!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_SIZE_INVALID(4034, "SizeId is not in the product size list!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_COLOR_NOT_EXISTS(4035, "ColorId not exists!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_COLOR_INVALID(4036, "ColorId is not in the product size list!", HttpStatus.BAD_REQUEST),//400
    DISTRICT_INVALID(4037, "DistrictId not in provinceId!", HttpStatus.BAD_REQUEST),//400
    WARD_INVALID(4038, "WardId not in districtId!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_SIZE_REQUIRE(4039, "SizeId is require (>0)!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_COLOR_REQUIRE(4040, "ColorId is require (>0)!", HttpStatus.BAD_REQUEST),//400
    CANT_ADD_COLOR(4041, "Cannot add color because this product is already import without color!", HttpStatus.BAD_REQUEST),//400
    CANT_ADD_SIZE(4041, "Cannot add size because this product is already import without size!", HttpStatus.BAD_REQUEST),//400
    COLOR_EXIDTED(4041, "ColorName is already exists!", HttpStatus.BAD_REQUEST),//400
    SIZE_EXIDTED(4042, "Size is already exists!", HttpStatus.BAD_REQUEST),//400
    QUANTITY_INVALID(4043, "Quantity must be greater than 0!", HttpStatus.BAD_REQUEST),//400
    PRICE_INVALID(4044, "Price must be greater than 0!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_IMPORT_NOT_EXISTS(4045, "ImportId not exists!", HttpStatus.BAD_REQUEST),//400
    CANT_UPDATE_IMPORT(4046, "Cannot update because there are no changes!", HttpStatus.BAD_REQUEST),//400
    CART_ITEM_INVAID_SIZE(4047, "SizeId not exists or sizeId not in list size of product!", HttpStatus.BAD_REQUEST),//400
    CART_ITEM_INVAID_COLOR(4048, "ColorId not exists or colorId not in list size of product!", HttpStatus.BAD_REQUEST),//400
    TOKEN_EXPIRED(4049, "AccessToken has expired, please refresh accessToken or log in again!", HttpStatus.BAD_REQUEST),//400
    QUANTYTI_INSUFFICIENT(4050, "The remaining quantity of products by size and color is not enough!", HttpStatus.BAD_REQUEST),//400
    CART_ITEM_NOT_EXISTS(4051, "CartItemId not exists!", HttpStatus.BAD_REQUEST),//400
    CART_ITEM_UNAUTH(4052, "You do not have permission to operate on this cartItem because this cartItem not yours!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_TYPE_INVALID(4053, VoucherType.getError(), HttpStatus.BAD_REQUEST),//400
    VOUCHER_CODE_EXISTED(4054, "Voucher code existed!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_CODE_INVALD(4055, "Voucher code must be at least 6 characters!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_PERCENT_DECREASE_INVALD(4056, "Percent decrease must be between 0 and 100!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_MIN_PRICE_INVALD(4057, "Min price must be greater than or equal to 0!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_QUANTITY_INVALD(4058, "Quantity must be greater than or equal to 0!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_END_AT_INVALD(4059, "EndAt must come after (>=) StartAt!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_CODE_NOT_EXISTS(4060, "Voucher code not exists or expired!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_NOT_EXISTS(4061, "VoucherId not exists!", HttpStatus.BAD_REQUEST),//400
    CATEGORY_DUPLICATE(4062, "CategoryId cannot be duplicated!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_EXPIRED_QUANTITY(4063, "Voucher quantity has expired!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_CANT_APPLY(4064, "Not eligible to apply voucher!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_EXPIRED(4065, "Voucher has expired!", HttpStatus.BAD_REQUEST),//400
    CART_ITEM_REQUIRED(4066, "CartItemIds cannot empty!", HttpStatus.BAD_REQUEST),//400
    SHIPPING_COST_REQUIRED(4067, "Shipping cost is required!", HttpStatus.BAD_REQUEST),//400
    DELIVERY_METHOD_INVALID(4068, "Delivery method invalid!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_SHIP_INVALID(4069, "Cannot use ShopVoucher for ShipVoucher!", HttpStatus.BAD_REQUEST),//400
    VOUCHER_SHOP_INVALID(4069, "Cannot use ShipVoucher for ShopVoucher!", HttpStatus.BAD_REQUEST),//400
    CART_IS_EMPTY(4070, "There are no products in the cart!", HttpStatus.BAD_REQUEST),//400
    CART_ITEM_DUPLICATED(4071, "CartItemId cannot duplicated!", HttpStatus.BAD_REQUEST),//400
    PRODUCT_QUANTITY_NOT_ENOUGH(4072, "Product quantity not enough!", HttpStatus.BAD_REQUEST),//400
    ORDER_NOT_EXISTS(4073, "OrderId not exists!", HttpStatus.BAD_REQUEST),//400
    ORDER_CANT_CANCEL(4074, "Cannot cancel this order!", HttpStatus.BAD_REQUEST),//400
    ORDER_UPDATE_UNAUTH(4075, "You do not have permission to update this order!", HttpStatus.BAD_REQUEST),//400
    ORDER_CANT_UPDATE(4076, "Cannot update this order status!", HttpStatus.BAD_REQUEST),//400
    ORDER_STATUS_INVALID(4077, "Order status invalid, order status must in: "+ OrderStatus.getAllStatus(), HttpStatus.BAD_REQUEST),//400
    PRODUCT_REVIEW_EXISTED(4078, "Product review with orderId and productId existed", HttpStatus.BAD_REQUEST),//400
    PRODUCT_REVIEW_NOT_EXIST(4079, "Order not contain productId", HttpStatus.BAD_REQUEST),//400
    PRODUCT_REVIEW_UNAUTH(4080, "You do not have permision to review with this orderId and productId", HttpStatus.BAD_REQUEST),//400
    RATING_SCORE_INVALID(4081, "Rating score must between 1 and 5", HttpStatus.BAD_REQUEST),//400
    PRODUCT_REVIEW_STATUS_INVALID(4082, "Orders must have been successfully delivered to be review!", HttpStatus.BAD_REQUEST),//400
    EMAIL_NOT_EXISTS(4083, "Email not exists!", HttpStatus.BAD_REQUEST),//400
    USERNAME_OR_EMAIL_NOT_EXISTS(4084, "Username or Email not exists!", HttpStatus.BAD_REQUEST),//400
    USERNAME_OR_EMAIL_INVALID_OTP(4085, "OTP for this Username or Email not exists!", HttpStatus.BAD_REQUEST),//400
    OTP_INCORRECT(4086, "OTP is incorrect!", HttpStatus.BAD_REQUEST),//400
    OTP_EXPIRED(4086, "OTP has expired!", HttpStatus.BAD_REQUEST),//400
    OTP_VERIFIED(4086, "OTP has verified, please reset password!", HttpStatus.BAD_REQUEST),//400
    RESET_TOKEN_INVALID(4087, "Reset passowd token invalid!", HttpStatus.BAD_REQUEST),//400
    INVALID_KEY(400, "Invalid message key!", HttpStatus.BAD_REQUEST),
    INVALID_DOB(400, "Invalid date of birth!", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(400, "Invalid token!", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(400, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),// 500
    SERVER_ERROR(5001, "There is an error from the server, please try again later!", HttpStatus.INTERNAL_SERVER_ERROR)// 500
     ,UNAUTHORIZED(400, "You do not have permission", HttpStatus.FORBIDDEN) //403
    ;
    private int code;
    private boolean isSuccess;
    private String message;
    private HttpStatusCode statusCode;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.isSuccess = false;
        this.message = message;
        this.statusCode = statusCode;
    }
    public ErrorCode setMessage(String message){
        this.message = message;
        return this;
    }

}

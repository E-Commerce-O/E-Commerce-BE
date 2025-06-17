package org.example.cdweb_be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.context.MessageSource;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(4001, "unauthenticated", HttpStatus.UNAUTHORIZED),
    TAG_EXISTED(4000, "tag.existed", HttpStatus.BAD_REQUEST),
    IMAGE_REQUIRED(4002, "image.required", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(4003, "category.existed", HttpStatus.BAD_REQUEST),
    NOT_FOUND(4004, "not.found", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTS(4005, "category.not.exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(4006, "username.invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(4007, "password.invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(4008, "email.invalid", HttpStatus.BAD_REQUEST),
    PHONENUMBER_INVALID(4009, "phone.number.invalid", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(4010, "username.existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(4011, "email.existed", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(4012, "phone.number.existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(4013, "user.not.exists", HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT(4014, "password.incorrect", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND(4015, "refresh.token.not.found", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID(4016, "refresh.token.invalid", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_INVALID(4017, "access.token.invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTS(4018, "product.not.exists", HttpStatus.BAD_REQUEST),
    PRODUCT_DETAIL_NOT_EXISTS(4019, "product.detail.not.exists", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_EXISTS(4020, "image.not.exists", HttpStatus.BAD_REQUEST),
    IMAGE_INVALID(4021, "image.invalid", HttpStatus.BAD_REQUEST),
    ADD_TAG_FAILED(4022, "add.tag.failed", HttpStatus.BAD_REQUEST),
    PRODUCT_TAG_NOT_EXISTS(4023, "product.tag.not.exists", HttpStatus.BAD_REQUEST),
    PRODUCT_TAG_EMPTY(4024, "product.tag.empty", HttpStatus.BAD_REQUEST),
    IMAGE_PATHS_EMPTY(4025, "image.paths.empty", HttpStatus.BAD_REQUEST),
    PROVINCE_NOT_EXISTS(4026, "province.not.exists", HttpStatus.BAD_REQUEST),
    DISTRICT_NOT_EXISTS(4027, "district.not.exists", HttpStatus.BAD_REQUEST),
    WARD_NOT_EXISTS(4028, "ward.not.exists", HttpStatus.BAD_REQUEST),
    ADDRESS_EXISTED(4029, "address.existed", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_EXISTS(4030, "address.not.exists", HttpStatus.BAD_REQUEST),
    ADDRESS_UNAUTHORIZED(4031, "address.unauthorized", HttpStatus.BAD_REQUEST),
    WISHLIST_EXISTED(4032, "wishlist.existed", HttpStatus.BAD_REQUEST),
    WISHLIST_NOT_EXISTS(4033, "wishlist.not.exists", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_NOT_EXISTS(4034, "product.size.not.exists", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_INVALID(4035, "product.size.invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_COLOR_NOT_EXISTS(4036, "product.color.not.exists", HttpStatus.BAD_REQUEST),
    PRODUCT_COLOR_INVALID(4037, "product.color.invalid", HttpStatus.BAD_REQUEST),
    DISTRICT_INVALID(4038, "district.invalid", HttpStatus.BAD_REQUEST),
    WARD_INVALID(4039, "ward.invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_REQUIRE(4040, "product.size.require", HttpStatus.BAD_REQUEST),
    PRODUCT_COLOR_REQUIRE(4041, "product.color.require", HttpStatus.BAD_REQUEST),
    CANT_ADD_COLOR(4042, "cant.add.color", HttpStatus.BAD_REQUEST),
    CANT_ADD_SIZE(4043, "cant.add.size", HttpStatus.BAD_REQUEST),
    COLOR_EXISTED(4044, "color.existed", HttpStatus.BAD_REQUEST),
    SIZE_EXISTED(4045, "size.existed", HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(4046, "quantity.invalid", HttpStatus.BAD_REQUEST),
    PRICE_INVALID(4047, "price.invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_IMPORT_NOT_EXISTS(4048, "product.import.not.exists", HttpStatus.BAD_REQUEST),
    CANT_UPDATE_IMPORT(4049, "cant.update.import", HttpStatus.BAD_REQUEST),
    CART_ITEM_INVALID_SIZE(4050, "cart.item.invalid.size", HttpStatus.BAD_REQUEST),
    CART_ITEM_INVALID_COLOR(4051, "cart.item.invalid.color", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(4052, "token.expired", HttpStatus.BAD_REQUEST),
    QUANTITY_INSUFFICIENT(4053, "quantity.insufficient", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_EXISTS(4054, "cart.item.not.exists", HttpStatus.BAD_REQUEST),
    CART_ITEM_UNAUTH(4055, "cart.item.unauthorized", HttpStatus.BAD_REQUEST),
    VOUCHER_TYPE_INVALID(4056, "voucher.type.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_CODE_EXISTED(4057, "voucher.code.existed", HttpStatus.BAD_REQUEST),
    VOUCHER_CODE_INVALID(4058, "voucher.code.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_PERCENT_DECREASE_INVALID(4059, "voucher.percent.decrease.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_MIN_PRICE_INVALID(4060, "voucher.min.price.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_QUANTITY_INVALID(4061, "voucher.quantity.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_END_AT_INVALID(4062, "voucher.end.at.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_CODE_NOT_EXISTS(4063, "voucher.code.not.exists", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_EXISTS(4064, "voucher.not.exists", HttpStatus.BAD_REQUEST),
    CATEGORY_DUPLICATE(4065, "category.duplicate", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED_QUANTITY(4066, "voucher.expired.quantity", HttpStatus.BAD_REQUEST),
    VOUCHER_CANT_APPLY(4067, "voucher.cant.apply", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED(4068, "voucher.expired", HttpStatus.BAD_REQUEST),
    CART_ITEM_REQUIRED(4069, "cart.item.required", HttpStatus.BAD_REQUEST),
    SHIPPING_COST_REQUIRED(4070, "shipping.cost.required", HttpStatus.BAD_REQUEST),
    DELIVERY_METHOD_INVALID(4071, "delivery.method.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_SHIP_INVALID(4072, "voucher.ship.invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_SHOP_INVALID(4073, "voucher.shop.invalid", HttpStatus.BAD_REQUEST),
    CART_IS_EMPTY(4074, "cart.is.empty", HttpStatus.BAD_REQUEST),
    CART_ITEM_DUPLICATED(4075, "cart.item.duplicated", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_NOT_ENOUGH(4076, "product.quantity.not.enough", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTS(4077, "order.not.exists", HttpStatus.BAD_REQUEST),
    ORDER_CANT_CANCEL(4078, "order.cant.cancel", HttpStatus.BAD_REQUEST),
    ORDER_UPDATE_UNAUTH(4079, "order.update.unauthorized", HttpStatus.BAD_REQUEST),
    ORDER_CANT_UPDATE(4080, "order.cant.update", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_INVALID(4081, "order.status.invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_REVIEW_EXISTED(4082, "product.review.existed", HttpStatus.BAD_REQUEST),
    PRODUCT_REVIEW_NOT_EXIST(4083, "product.review.not.exist", HttpStatus.BAD_REQUEST),
    PRODUCT_REVIEW_UNAUTH(4084, "product.review.unauthorized", HttpStatus.BAD_REQUEST),
    RATING_SCORE_INVALID(4085, "rating.score.invalid", HttpStatus.BAD_REQUEST),
    PRODUCT_REVIEW_STATUS_INVALID(4086, "product.review.status.invalid", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTS(4087, "email.not.exists", HttpStatus.BAD_REQUEST),
    USERNAME_OR_EMAIL_NOT_EXISTS(4088, "username.or.email.not.exists", HttpStatus.BAD_REQUEST),
    USERNAME_OR_EMAIL_INVALID_OTP(4089, "username.or.email.invalid.otp", HttpStatus.BAD_REQUEST),
    OTP_INCORRECT(4090, "otp.incorrect", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(4091, "otp.expired", HttpStatus.BAD_REQUEST),
    OTP_VERIFIED(4092, "otp.verified", HttpStatus.BAD_REQUEST),
    RESET_TOKEN_INVALID(4093, "reset.token.invalid", HttpStatus.BAD_REQUEST),
    TAG_NOT_EXISTS(4094, "tag.not.exists", HttpStatus.BAD_REQUEST),
    PAYMENT_METHOD_INVALID(4095, "payment.method.invalid", HttpStatus.BAD_REQUEST),
    FILE_ISNT_IMAGE(4096, "file.isnt.image", HttpStatus.BAD_REQUEST),
    FILE_IS_EMPTY(4097, "file.is.empty", HttpStatus.BAD_REQUEST),
    PRODUCT_DONT_HAVE_COLOR(4098, "product.detail.color", HttpStatus.BAD_REQUEST),
    PRODUCT_DONT_HAVE_SIZE(4099, "product.detail.size", HttpStatus.BAD_REQUEST),
    PRODUCT_DONT_HAVE_SIZE_AND_COLOR(4100, "product.detail.size.color", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_INCORRECT(4101, "user.confirm.password", HttpStatus.BAD_REQUEST),
    PASSWORD_NO_CHANGE(4102, "user.password.no.change", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(4103, "user.role.invalid", HttpStatus.BAD_REQUEST),
    CANT_CHANGE_ADMIN_ROLE(4104, "user.role.admin", HttpStatus.BAD_REQUEST),
    ROLE_NO_HAVE_CHANGE(4105, "user.role.no.have.change", HttpStatus.BAD_REQUEST),
    INVALID_KEY(400, "invalid.key", HttpStatus.BAD_REQUEST),
    INVALID_DOB(401, "invalid.dob", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(402, "invalid.token", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(500, "uncategorized.exception", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_ERROR(5001, "server.error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(403, "unauthorized", HttpStatus.FORBIDDEN);

    private final int code;
    private final String messageKey;
    private final HttpStatus statusCode;

    ErrorCode(int code, String messageKey, HttpStatus statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }
    public boolean isSuccess(){
        return false;
    }
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
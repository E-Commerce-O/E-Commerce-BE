package org.example.cdweb_be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    TAG_EXISTED(4001, "Tag name existed!", HttpStatus.BAD_REQUEST),
    IMAGE_REQUIRED(4002, "Image file is required!", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(4003, "Category name existed!", HttpStatus.BAD_REQUEST),
    NOT_FOUND(4004, "Not found!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTS(4005, "CategoryId not exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(4006, "Username must be at least 6 characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(4007, "Password must be at least 6 characters!", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(4008, "Email invalid!", HttpStatus.BAD_REQUEST),
    PHONENUMER_INVALID(4009, "Email invalid!", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(4010, "Username existed!", HttpStatus.BAD_REQUEST), //400
    EMAIL_EXISTED(4011, "Email existed!", HttpStatus.BAD_REQUEST), //400
    PHONE_NUMBER_EXISTED(4012, "Phone number existed!", HttpStatus.BAD_REQUEST), //400
    USER_NOT_EXISTS(4013, "Username not exists!", HttpStatus.BAD_REQUEST),//404
    PASSWORD_INCORRECT(4014, "Incorrect password!", HttpStatus.BAD_REQUEST),//404
    REFRESH_TOKEN_NOT_FOUND(4015, "Incorrect refresh token!", HttpStatus.BAD_REQUEST),//404
    REFRESH_TOKEN_INVALID(4015, "Invalid refresh token!", HttpStatus.BAD_REQUEST),//404
    INVALID_KEY(400, "Invalid message key!", HttpStatus.BAD_REQUEST),
    INVALID_DOB(400, "Invalid date of birth!", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(400, "Invalid token!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(400, "Unauthenticated!", HttpStatus.UNAUTHORIZED),//401,
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


}

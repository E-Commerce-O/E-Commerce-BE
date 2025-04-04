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
    CATEGORY_NOT_EXISTS(4004, "CategoryId not exists", HttpStatus.BAD_REQUEST),
    USER_EXISTED(400, "Username existed!", HttpStatus.BAD_REQUEST), //400
    USER_NOT_EXISTS(400, "User not exists!", HttpStatus.NOT_FOUND),//404
    INVALID_KEY(400, "Invalid message key!", HttpStatus.BAD_REQUEST),
    INVALID_DOB(400, "Invalid date of birth!", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(400, "Invalid token!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(400, "Unauthenticated!", HttpStatus.UNAUTHORIZED),//401
    USERNAME_INVALID(400, "Username must be at least 3 characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Password must be at least 8 characters!", HttpStatus.BAD_REQUEST) //400
    ,UNCATEGORIZED_EXCEPTION(400, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR)// 500
    ,SERVER_ERROR(5001, "There is an error from the server, please try again later!", HttpStatus.INTERNAL_SERVER_ERROR)// 500
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

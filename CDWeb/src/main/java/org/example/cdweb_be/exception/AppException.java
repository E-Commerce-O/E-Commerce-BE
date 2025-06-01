package org.example.cdweb_be.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;

import java.nio.charset.StandardCharsets;

//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(MessageProvider messageProvider, ErrorCode errorCode) {
        super(messageProvider.getMessage(errorCode.getMessageKey())); // Lấy thông điệp từ MessageProvider
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
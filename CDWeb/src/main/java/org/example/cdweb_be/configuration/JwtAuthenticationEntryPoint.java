package org.example.cdweb_be.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    MessageProvider messageProvider;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException  { // hàm được gọi khi có exception trong khi authenticate
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse apiResponse = ApiResponse.builder()

                .code(errorCode.getCode())
                .isSuccess(errorCode.isSuccess())
                .data(messageProvider.getMessage(errorCode.getMessageKey()))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getStatusCode().value());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
            }
}

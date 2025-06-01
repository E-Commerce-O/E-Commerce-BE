package org.example.cdweb_be.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

@Component
@Slf4j
public class LanguageFilter implements Filter {

    @Autowired
    private MessageProvider MessageProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
       String acceptLanguage = httpRequest.getHeader("Accept-Language");
        log.info("acceptLanguage :"+acceptLanguage);
        Locale locale = httpRequest.getLocale();
        MessageProvider.setLocale(acceptLanguage);

        chain.doFilter(request, response);
    }
}
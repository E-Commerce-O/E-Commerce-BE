package org.example.cdweb_be.component;

import jakarta.servlet.http.HttpServletResponse;
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
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String logRequest = ("URI: '" + httpRequest.getRequestURI()+"' Method: "+ httpRequest.getMethod()) ;

       String acceptLanguage = httpRequest.getHeader("Accept-Language");
        MessageProvider.setLocale(acceptLanguage);

        chain.doFilter(request, response);
        logRequest+=(" Response status: " + httpResponse.getStatus());
        System.out.println(logRequest);
    }
}
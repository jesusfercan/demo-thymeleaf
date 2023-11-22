package com.example.demothymeleaf.security;

import com.example.demothymeleaf.entity.RecaptchaResponse;
import com.example.demothymeleaf.service.RecaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RecaptchaFilter extends OncePerRequestFilter {


    private final RecaptchaService recaptchaService;
    private final String failureUrl;


    public RecaptchaFilter(RecaptchaService recaptchaService, String failureUrl) {
        this.recaptchaService = recaptchaService;
        this.failureUrl = failureUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean filterContinue = true;
        if(request.getMethod().equals("POST") ) {
            String recaptcha = request.getHeader("recaptcha");
            recaptcha = request.getParameter("g-recaptcha-response");
            RecaptchaResponse recaptchaResponse = recaptchaService.validateToken(recaptcha);
            if(!recaptchaResponse.success()) {
                response.sendRedirect(failureUrl);
                filterContinue = false;
            }
        }
        if(filterContinue)
            filterChain.doFilter(request,response);
    }
}

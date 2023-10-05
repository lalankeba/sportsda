package com.laan.sportsda.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
@Order(2)
@Slf4j
@RequiredArgsConstructor
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        log.info("Starting transaction for: {}, with query string: {}, from: {}, using method: {}, via scheme: {}, through agent: {}",
                request.getRequestURL(), request.getQueryString(), request.getRemoteAddr(), request.getMethod(), request.getScheme(),
                request.getHeader(HttpHeaders.USER_AGENT));

        var start = Instant.now();
        chain.doFilter(request, response);
        var finish = Instant.now();

        log.info("Finished transaction for: {} in {} millis with status: {}", request.getRequestURL(), Duration.between(start, finish).toMillis(), response.getStatus());
    }
}

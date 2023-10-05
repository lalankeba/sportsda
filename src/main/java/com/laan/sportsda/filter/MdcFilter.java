package com.laan.sportsda.filter;

import com.laan.sportsda.util.IdGenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
@RequiredArgsConstructor
public class MdcFilter extends OncePerRequestFilter {

    private static final String MDC_KEY = "mdcId";

    private final IdGenUtil idGenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String mdcId = idGenUtil.get20LengthId();
        MDC.put(MDC_KEY, mdcId);
        chain.doFilter(request, response);
        MDC.remove(MDC_KEY);
    }
}

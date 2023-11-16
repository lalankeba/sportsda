package com.laan.sportsda.filter;

import com.laan.sportsda.mapper.MemberMapper;
import com.laan.sportsda.security.MemberDetails;
import com.laan.sportsda.service.SessionService;
import com.laan.sportsda.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final MemberMapper memberMapper;

    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getTokenFromRequest(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String id = jwtUtil.extractId(token);
            if (sessionService.validateSessionById(id)) {
                String username = jwtUtil.extractUsername(token);
                List<String> permissions = jwtUtil.extractPermissions(token);
                MemberDetails memberDetails = memberMapper.mapJwtToDetails(username, permissions);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }

}

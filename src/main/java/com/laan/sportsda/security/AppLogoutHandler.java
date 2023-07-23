package com.laan.sportsda.security;

import com.laan.sportsda.entity.SessionEntity;
import com.laan.sportsda.repository.SessionRepository;
import com.laan.sportsda.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;

    private final SessionRepository sessionRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = jwtUtil.getTokenFromRequest(request);
        if (token != null && jwtUtil.validateToken(token)) {
            String id = jwtUtil.extractId(token);
            Optional<SessionEntity> optionalStoredSessionEntity = sessionRepository.findById(id);
            if (optionalStoredSessionEntity.isPresent()) {
                SessionEntity sessionEntity = optionalStoredSessionEntity.get();
                if (!sessionEntity.getLoggedOut()) {
                    sessionEntity.setLoggedOut(true);
                    sessionEntity.setLogoutDateTime(new Date());
                    sessionRepository.save(sessionEntity);

                    log.info("Saved logout info: {}", sessionEntity);
                } else {
                    log.info("Already logged out. Nothing to save");
                }
            }
        }
    }
}

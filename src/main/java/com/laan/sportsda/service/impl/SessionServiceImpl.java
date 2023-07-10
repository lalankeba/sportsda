package com.laan.sportsda.service.impl;

import com.laan.sportsda.entity.SessionEntity;
import com.laan.sportsda.repository.SessionRepository;
import com.laan.sportsda.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public boolean validateSessionById(final String id) {
        boolean valid = false;
        Optional<SessionEntity> optionalSessionEntity = sessionRepository.findById(id);
        if (optionalSessionEntity.isPresent()) {
            SessionEntity sessionEntity = optionalSessionEntity.get();
            if (!sessionEntity.getLoggedOut()) {
                valid = true;
            }
        }
        return valid;
    }
}

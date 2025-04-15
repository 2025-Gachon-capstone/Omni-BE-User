package org.example.omnibeuser.service;

import org.example.omnibeuser.entity.Session;
import org.example.omnibeuser.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void create(String loginId, String refresh, Long expiredMs) {

        LocalDateTime expirationTime = LocalDateTime.now()
                .plusSeconds(expiredMs / 1000);

        Session session = Session.builder()
                .loginId(loginId)
                .refresh(refresh)
                .expired(expirationTime)
                .build();

        sessionRepository.save(session);

    }
}

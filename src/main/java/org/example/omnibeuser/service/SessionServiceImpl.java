package org.example.omnibeuser.service;

import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.exception.GeneralException;
import org.example.omnibeuser.common.security.JWTUtil;
import org.example.omnibeuser.entity.Session;
import org.example.omnibeuser.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final JWTUtil jwtUtil;

    public SessionServiceImpl(SessionRepository sessionRepository, JWTUtil jwtUtil) {
        this.sessionRepository = sessionRepository;
        this.jwtUtil = jwtUtil;
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

    @Transactional
    @Override
    public ApiResult<?> processLogout(String refresh) {

        if (!sessionRepository.existsByRefresh(refresh)) {
            return ApiResult.onFailure(ErrorStatus._NOTFOUND_REFRESH_TOKEN.getCode(),ErrorStatus._NOTFOUND_REFRESH_TOKEN.getMessage(),null);
        }

        try {
            sessionRepository.deleteByLoginId(jwtUtil.getLoginId(refresh));
        } catch (Exception e) {
            return ApiResult.onFailure(ErrorStatus._LOGOUT_FAILED.getCode(),ErrorStatus._LOGOUT_FAILED.getMessage(),null);
        }

        return ApiResult.onSuccess();
    }
}

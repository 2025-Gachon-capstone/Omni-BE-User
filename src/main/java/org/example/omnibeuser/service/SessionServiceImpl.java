package org.example.omnibeuser.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.exception.GeneralException;
import org.example.omnibeuser.common.security.JWTUtil;
import org.example.omnibeuser.common.util.CookieUtil;
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

    @Override
    @Transactional
    public ApiResult<?> refreshToken(String refresh, HttpServletResponse response) {

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ApiResult.onFailure(ErrorStatus._NULL_REFRESH_TOKEN.getCode(), ErrorStatus._NULL_REFRESH_TOKEN.getMessage(), null);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ApiResult.onFailure(ErrorStatus._EXFIRED_REFRESH_TOKEN.getCode(), ErrorStatus._EXFIRED_REFRESH_TOKEN.getMessage(), null);
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ApiResult.onFailure(ErrorStatus._INVALID_REFRESH_TOKEN.getCode(), ErrorStatus._INVALID_REFRESH_TOKEN.getMessage(), null);
        }

        if (!sessionRepository.existsByRefresh(refresh)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ApiResult.onFailure(ErrorStatus._NOTFOUND_REFRESH_TOKEN.getCode(), ErrorStatus._NOTFOUND_REFRESH_TOKEN.getMessage(), null);
        }

        String loginId = jwtUtil.getLoginId(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", loginId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", loginId, role, 86400000L);

        sessionRepository.deleteByLoginId(loginId);
        create(loginId,newRefresh,86400000L);

        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(CookieUtil.createHttpOnlyCookie("refresh", newRefresh));

        return ApiResult.onSuccess();
    }

}

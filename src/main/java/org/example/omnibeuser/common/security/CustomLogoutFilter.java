package org.example.omnibeuser.common.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.util.CookieUtil;
import org.example.omnibeuser.common.util.JsonResponseUtil;
import org.example.omnibeuser.repository.SessionRepository;
import org.example.omnibeuser.service.SessionService;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final SessionService sessionService;

    public CustomLogoutFilter(JWTUtil jwtUtil, SessionService sessionService) {
        this.jwtUtil = jwtUtil;
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);

    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 경로가 logout , POST 방식인지 확인
        String requestUri = request.getRequestURI();
        if (!requestUri.endsWith("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 refresh 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        // 쿠키가 null인지 확인
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {

            // ApiResult 생성
            ApiResult<?> apiResult = ApiResult.onFailure(ErrorStatus._NULL_REFRESH_TOKEN.getCode(),ErrorStatus._NULL_REFRESH_TOKEN.getMessage(), null);
            JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, apiResult);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // ApiResult 생성
            ApiResult<?> apiResult = ApiResult.onFailure(ErrorStatus._EXFIRED_REFRESH_TOKEN.getCode(),ErrorStatus._EXFIRED_REFRESH_TOKEN.getMessage(), null);
            JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, apiResult);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            // ApiResult 생성
            ApiResult<?> apiResult = ApiResult.onFailure(ErrorStatus._INVALID_REFRESH_TOKEN.getCode(),ErrorStatus._INVALID_REFRESH_TOKEN.getMessage(), null);
            JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, apiResult);
            return;
        }

        ApiResult<?> logoutResult = sessionService.processLogout(refresh);

        // 실패한 경우
        if (!logoutResult.getIsSuccess()) {
            JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, logoutResult);
            return;
        }

        Cookie cookie = CookieUtil.createExpiredCookie("refresh", null);
        response.addCookie(cookie);

        // 로그아웃 성공 응답 설정
        ApiResult<?> successResult = ApiResult.onSuccess();
        JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_OK, successResult);
    }
}

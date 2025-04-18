package org.example.omnibeuser.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.omnibeuser.common.apiPayload.ApiResult;

public interface SessionService {

    void create(Long memberId,String refresh,Long expiredMs);
    ApiResult<?> processLogout(String refresh);
    ApiResult<?> refreshToken(String refresh, HttpServletResponse response);
    void deleteSession(Long memberId);

}

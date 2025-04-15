package org.example.omnibeuser.service;

import org.example.omnibeuser.common.apiPayload.ApiResult;

public interface SessionService {
    void create(String loginId,String refresh,Long expiredMs);
    ApiResult<?> processLogout(String refresh);
}

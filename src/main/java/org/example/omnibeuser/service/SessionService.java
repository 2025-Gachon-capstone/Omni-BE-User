package org.example.omnibeuser.service;

public interface SessionService {
    void create(String loginId,String refresh,Long expiredMs);
}

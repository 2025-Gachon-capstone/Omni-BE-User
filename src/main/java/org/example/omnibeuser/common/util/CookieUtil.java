package org.example.omnibeuser.common.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie createHttpOnlyCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // HTTPS만 허용 시
        // cookie.setPath("/");    // 필요에 따라 경로 설정
        return cookie;
    }

}

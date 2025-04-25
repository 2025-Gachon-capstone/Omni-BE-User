package org.example.omnibeuser.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static Cookie createHttpOnlyCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS만 허용 시
        cookie.setPath("/");    // 필요에 따라 경로 설정
        return cookie;
    }

    public static Cookie createExpiredCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(0);
        cookie.setPath("/");    // 필요에 따라 경로 설정
        cookie.setSecure(true); // HTTPS만 허용 시
        return cookie;
    }

}

package org.example.omnibeuser.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    // ✅ refresh 쿠키 생성 (SameSite=None, Secure, HttpOnly 포함)
    public static void addSameSiteCookie(HttpServletResponse response, String key, String value, int maxAgeSeconds) {
        String cookie = key + "=" + value + "; " +
                "Max-Age=" + maxAgeSeconds + "; " +
                "Path=/; " +
                "HttpOnly; " +
                "Secure; " +
                "SameSite=None";

        response.addHeader("Set-Cookie", cookie);
    }

    // ✅ refresh 쿠키 만료 (삭제)
    public static void expireSameSiteCookie(HttpServletResponse response, String key) {
        String cookie = key + "=; " +
                "Max-Age=0; " +
                "Path=/; " +
                "HttpOnly; " +
                "Secure; " +
                "SameSite=None";

        response.addHeader("Set-Cookie", cookie);
    }

}

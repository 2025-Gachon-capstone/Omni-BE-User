package org.example.omnibeuser.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addSameSiteCookie(HttpServletResponse response, String key, String value, int maxAgeSeconds) {
        String cookie = key + "=" + value + "; " +
                "Max-Age=" + maxAgeSeconds + "; " +
                "Path=/; " +
                "HttpOnly; " +
                "Secure; " +
                "SameSite=None";

        response.addHeader("Set-Cookie", cookie);
    }

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

package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/v1/auth")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급 API",description = "로그인 후 사용이 가능합니다.",tags = "Session")
    public ApiResult<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }
        return sessionService.refreshToken(refresh, response);
    }

}

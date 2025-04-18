package org.example.omnibeuser.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.omnibeuser.client.SponsorClient;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.code.status.SuccessStatus;
import org.example.omnibeuser.common.util.CookieUtil;
import org.example.omnibeuser.common.util.JsonResponseUtil;
import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.dto.SponsorResDto;
import org.example.omnibeuser.service.SessionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final SessionService sessionService;
    private final SponsorClient sponsorClient;


    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       SessionService sessionService, SponsorClient sponsorClient) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.sessionService = sessionService;
        this.sponsorClient = sponsorClient;
        setFilterProcessesUrl("/user/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        MemberReqDto.Login loginDto = new MemberReqDto.Login();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = objectMapper.readValue(messageBody, MemberReqDto.Login.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String loginId = loginDto.getLoginId();
        String password = loginDto.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        //UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String loginId = customUserDetails.getUsername();
        Long memberId = customUserDetails.getMemberId();
        String memberName = customUserDetails.getMemberName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // sponsorId 조회
        Long sponsorId = null;
        if ("SPONSOR".equals(role)) {
            try {
                ApiResult<SponsorResDto.GetSponsorId> sponsorRes = sponsorClient.getSponsorId(memberId);
                sponsorId = sponsorRes.getResult().getSponsorId();
            } catch (Exception e) {
                ApiResult<?> failResult = ApiResult.onFailure(ErrorStatus._SPONSOR_SERVICE_ERROR.getCode(),ErrorStatus._SPONSOR_SERVICE_ERROR.getMessage(),null);
                JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, failResult);
                return;
            }
        }

        String access = jwtUtil.createJwt("access",loginId, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh",loginId, role, 86400000L);

        sessionService.create(loginId, refresh, 86400000L);

        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(CookieUtil.createHttpOnlyCookie("refresh", refresh));

        // ApiResult 생성
        ApiResult<?> apiResult;
        if ("SPONSOR".equals(role)) {
            apiResult = ApiResult.onSuccess(SuccessStatus._OK,
                    new MemberResDto.SponsorLogin(memberId, sponsorId, loginId, memberName, role));
        } else {
            apiResult = ApiResult.onSuccess(SuccessStatus._OK,
                    new MemberResDto.UserLogin(memberId, loginId, memberName, role));
        }

        JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_OK, apiResult);

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ApiResult<?> apiResult = ApiResult.onFailure(ErrorStatus._LOGIN_FAILED.getCode(), ErrorStatus._LOGIN_FAILED.getMessage(), null);
        JsonResponseUtil.sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, apiResult);
    }

}

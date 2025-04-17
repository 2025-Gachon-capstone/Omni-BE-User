package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.util.CookieUtil;
import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.service.MemberService;
import org.example.omnibeuser.service.MemberServiceImpl;
import org.example.omnibeuser.service.SessionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final SessionService sessionService;

    public MemberController(MemberService memberService, SessionService sessionService) {
        this.memberService = memberService;
        this.sessionService = sessionService;
    }

    @PostMapping("/signup/normal")
    @Operation(summary = "일반 사용자 회원가입 API",description = "일반 사용자 회원가입을 위한 Api입니다.",tags = "Member")
    public ApiResult<MemberResDto.NormalSignup> createNormalUser(@Valid @RequestBody MemberReqDto.NormalSignup normalSignupReqDto) {

        Member savedMember = memberService.createNormalMember(normalSignupReqDto);
        return ApiResult.onSuccess(MemberResDto.NormalSignup.from(savedMember));

    }

    @PostMapping("/signup/sponsor")
    @Operation(summary = "협찬사 사용자 회원가입 API",description = "협찬사 사용자 회원가입을 위한 Api입니다.",tags = "Member")
    public ApiResult<MemberResDto.SponsorSignup> createSponsorUser(@Valid @RequestBody MemberReqDto.SponsorSignup sponsorSignupDto) {

        Member savedMember = memberService.createSponsorMember(sponsorSignupDto);
        return ApiResult.onSuccess(MemberResDto.SponsorSignup.from(savedMember));

    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API",
            description = "로그인시 헤더에 발급되는 Authorization을 따로 보관해야 합니다.",
            tags = "Member")
    public ApiResult<?> login(@Valid @RequestBody MemberReqDto.Login loginReqDto) {

        return ApiResult.onSuccess();

    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API",
            description = "로그아웃시 따로 보관한 Authorization을 삭제해주셔야 합니다.",
            tags = "Member")
    public ApiResult<?> logout() {

        return ApiResult.onSuccess();
    }

    @PostMapping("/password/verify")
    @Operation(summary = "비밀번호 인증 API",
            description = "비밀번호 인증 Api 입니다.",
            tags = "Member")
    public ApiResult<?> verify(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") String loginId,
                               @Valid @RequestBody MemberReqDto.VerifyPassword passwordDto) {

        memberService.verifyPassword(loginId,passwordDto.getPassword());
        return ApiResult.onSuccess();
    }

    @PutMapping("/members")
    @Operation(summary = "정보 수정 API",
            description = "정보 수정 Api 입니다.",
            tags = "Member")
    public ApiResult<MemberResDto.UpdateMember> update(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") String loginId,
                                                       @Valid @RequestBody MemberReqDto.UpdateMember updateMemberDto){

        Member savedMember = memberService.updateMember(loginId,updateMemberDto);
        return ApiResult.onSuccess(new MemberResDto.UpdateMember(savedMember.getLoginId()));

    }

    @DeleteMapping("/resign")
    @Operation(summary = "회원 탈퇴 API",
            description = "탈퇴시 따로 보관한 Authorization을 삭제해주셔야 합니다.",
            tags = "Member")
    public ApiResult<MemberResDto.DeleteMember> resign(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") String loginId,
                               HttpServletResponse response){

        Member savedMember = memberService.deleteMember(loginId);
        sessionService.deleteSession(loginId);
        response.addCookie(CookieUtil.createExpiredCookie("refresh",null));
        return ApiResult.onSuccess(new MemberResDto.DeleteMember(savedMember.getLoginId()));

    }

}

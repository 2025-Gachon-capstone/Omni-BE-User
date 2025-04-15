package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.dto.MemberReqDto;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.entity.Member;
import org.example.omnibeuser.entity.type.Role;
import org.example.omnibeuser.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1/auth")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup/normal")
    @Operation(summary = "일반 사용자 회원가입 API",description = "일반 사용자 회원가입을 위한 Api입니다.",tags = "Member")
    public ApiResult<MemberResDto.NormalSignup> createNormalUser(@RequestBody MemberReqDto.NormalSignup normalSignupReqDto) {

        Member savedMember = memberService.createNormalMember(normalSignupReqDto);
        return ApiResult.onSuccess(MemberResDto.NormalSignup.from(savedMember));

    }

    @PostMapping("/signup/sponsor")
    @Operation(summary = "협찬사 사용자 회원가입 API",description = "협찬사 사용자 회원가입을 위한 Api입니다.",tags = "Member")
    public ApiResult<MemberResDto.SponsorSignup> createSponsorUser(@RequestBody MemberReqDto.SponsorSignup sponsorSignupDto) {

        Member savedMember = memberService.createSponsorMember(sponsorSignupDto);
        return ApiResult.onSuccess(MemberResDto.SponsorSignup.from(savedMember));

    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API",
            description = "로그인을 위한 Api 입니다.",
            tags = "Member")
    public ApiResult<?> login(@RequestBody MemberReqDto.Login loginReqDto) {

        return ApiResult.onSuccess();

    }

}

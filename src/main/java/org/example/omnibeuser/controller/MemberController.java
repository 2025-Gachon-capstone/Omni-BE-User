package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.List;

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
    @Operation(summary = "일반 사용자 회원가입 API",description = "일반 사용자 회원가입을 위한 Api입니다. ( 인증 필요없음 )",tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4001", description = "MEMBER4001-비밀번호가 동일하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4003", description = "MEMBER4003-이미 존재하는 아이디입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5002", description = "CARD5002-카드 생성 실패입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<MemberResDto.NormalSignup> createNormalUser(@Valid @RequestBody MemberReqDto.NormalSignup normalSignupReqDto) {

        Member savedMember = memberService.createNormalMember(normalSignupReqDto);
        return ApiResult.onSuccess(MemberResDto.NormalSignup.from(savedMember));

    }

    @PostMapping("/signup/sponsor")
    @Operation(summary = "협찬사 사용자 회원가입 API",description = "협찬사 사용자 회원가입을 위한 Api입니다. ( 인증 필요없음 )",tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4001", description = "MEMBER4001-비밀번호가 동일하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4003", description = "MEMBER4003-이미 존재하는 아이디입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5001", description = "SPONSOR5001-스폰서 생성 실패입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<MemberResDto.SponsorSignup> createSponsorUser(@Valid @RequestBody MemberReqDto.SponsorSignup sponsorSignupDto) {

        Member savedMember = memberService.createSponsorMember(sponsorSignupDto);
        return ApiResult.onSuccess(MemberResDto.SponsorSignup.from(savedMember));

    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API",
            description = "로그인시 헤더에 발급되는 Authorization을 따로 보관해야 합니다. ( 인증 필요없음 )",
            tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4002", description = "MEMBER4002-로그인에 실패했습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5002", description = "SERVICE5002-SPONSOR 서버 에러",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<?> login(@Valid @RequestBody MemberReqDto.Login loginReqDto) {

        return ApiResult.onSuccess();

    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API",
            description = "로그아웃시 따로 보관한 Authorization을 삭제해주셔야 합니다. ( 쿠키 필요 )",
            tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4002", description = "TOKEN4002-만료된 리프레쉬 토큰입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4003", description = "TOKEN4003-유효하지 않은 리프레쉬 토큰입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4004", description = "TOKEN4004-리프레쉬 토큰이 헤더에 없습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<?> logout() {

        return ApiResult.onSuccess();
    }

    @PostMapping("/password/verify")
    @Operation(summary = "비밀번호 인증 API",
            description = "비밀번호 인증 Api 입니다. ( 엑세스 토큰 필요 )",
            tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4001", description = "MEMBER4001-비밀번호가 동일하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<?> verify(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId,
                               @Valid @RequestBody MemberReqDto.VerifyPassword passwordDto) {

        memberService.verifyPassword(memberId,passwordDto.getPassword());
        return ApiResult.onSuccess();
    }

    @PutMapping("/members")
    @Operation(summary = "정보 수정 API",
            description = "정보 수정 Api 입니다. ( 엑세스 토큰 필요 )",
            tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4001", description = "MEMBER4001-비밀번호가 동일하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4005", description = "MEMBER4005-새로운 비밀번호가 동일하지 않습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4006", description = "MEMBER4006-기존 비밀번호와 동일합니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<MemberResDto.UpdateMember> update(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId,
                                                       @Valid @RequestBody MemberReqDto.UpdateMember updateMemberDto){

        Member savedMember = memberService.updateMember(memberId,updateMemberDto);
        return ApiResult.onSuccess(new MemberResDto.UpdateMember(savedMember.getMemberId()));

    }

    @DeleteMapping("/resign")
    @Operation(summary = "회원 탈퇴 API",
            description = "탈퇴시 따로 보관한 Authorization을 삭제해주셔야 합니다. ( 엑세스 토큰 필요 )",
            tags = "Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4007", description = "MEMBER4007-이미 탈퇴한 사용자입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<MemberResDto.DeleteMember> resign(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId,
                               HttpServletResponse response){

        Member savedMember = memberService.deleteMember(memberId);
        sessionService.deleteSession(Long.valueOf(memberId));
        CookieUtil.expireSameSiteCookie(response, "refresh");
        return ApiResult.onSuccess(new MemberResDto.DeleteMember(savedMember.getMemberId()));

    }

    @PostMapping("/memberList")
    @Operation(summary = "사용자 아이디 리스트로 사용자 정보 가져오기",
            description = "( 서비스 끼리 통신입니다. )",
            tags = "Service-Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<List<MemberResDto.GetMemberList>> getMemberList(@RequestBody List<Long> memberIdList) {

        return ApiResult.onSuccess(memberService.getMemberListByMemberId(memberIdList));

    }

    @GetMapping("/id")
    @Operation(summary = "로그인 아이디로 사용자 정보 가져오기",
            description = "( 서비스 끼리 통신입니다. )",
            tags = "Service-Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<MemberResDto.GetMemberByLoginId> getMemberByLoginId(@RequestParam String loginId) {

        return ApiResult.onSuccess(memberService.getMemberByLoginId(loginId));

    }

}

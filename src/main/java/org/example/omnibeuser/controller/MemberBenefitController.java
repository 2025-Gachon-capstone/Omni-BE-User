package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.dto.MemberBenefitReqDto;
import org.example.omnibeuser.dto.MemberBenefitResDto;
import org.example.omnibeuser.service.MemberBenefitService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/v1")
public class MemberBenefitController {

    private final MemberBenefitService memberBenefitService;

    public MemberBenefitController(MemberBenefitService memberBenefitService) {
        this.memberBenefitService = memberBenefitService;
    }

    @PostMapping("/memberBenefit")
    @Operation(summary = "멤버혜택 생성 API",description = "서비스 끼리 통신입니다.",tags = "Service-MemberBenefit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),@ApiResponse(responseCode = "4002-1", description = "CARD4002-사용자의 카드가 없습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4002-2", description = "BENEFIT4002-유효하지 않은 헤택 상태입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4003", description = "BENEFIT4003-혜택의 수량을 초과하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5002", description = "SERVICE5002-SPONSOR 서버 에러",content = @Content(schema = @Schema(implementation = ApiResult.class)))

    })
    public ApiResult<List<Long>> createMemberBenefit(@RequestBody MemberBenefitReqDto.CreateMemberBenefit createMemberBenefitDto){

        return ApiResult.onSuccess(memberBenefitService.createMemberBenefit(createMemberBenefitDto));
    }

    @GetMapping("/my/memberBenefits")
    @Operation(summary = "전체 멤버 혜택 Api",description = " 페이징 필요 - ( page, size 만 적어도 됨, 스웨거에서 sort 는 배열이 아닌 빈문자열로 넣어주세요 )  - ( 엑세스 토큰 필요 )",tags = "MemberBenefit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5002", description = "SERVICE5002-SPONSOR 서버 에러",content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<MemberBenefitResDto.GetMemberBenefitPage> getCardBenefits(@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                               @Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId){

        return ApiResult.onSuccess(memberBenefitService.getMemberBenefits(memberId, pageable));

    }

    @GetMapping("/my/memberBenefits/available")
    @Operation(summary = "사용가능한 멤버 혜택 Api",description = " 상태가 ONGOING인 멤버혜택만 보여집니다. - ( 엑세스 토큰 필요 )",tags = "MemberBenefit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5002", description = "SERVICE5002-SPONSOR 서버 에러",content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<MemberBenefitResDto.GetMemberBenefit>> getAvailableMemberBenefits(@Parameter(hidden = true) @RequestHeader("X-Authorization-Id") Long memberId){

        return ApiResult.onSuccess(memberBenefitService.getAvailableMemberBenefit(memberId));

    }

    @PostMapping("/memberBenefits/check")
    @Operation(summary = "멤버 혜택 확인하기 Api",description = " 카드 번호 16자리를 입력해주세요. - ( 토큰 필요 없음 )",tags = "MemberBenefit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5001", description = "SERVICE5001-CARD 서버 에러",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "5002", description = "SERVICE5002-SPONSOR 서버 에러",content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<MemberBenefitResDto.GetMemberBenefit>> checkAvailableMemberBenefits(@Valid @RequestBody MemberBenefitReqDto.CheckAvailableMemberBenefit checkAvailableMemberBenefit){

        return ApiResult.onSuccess(memberBenefitService.checkAvailableMemberBenefit(checkAvailableMemberBenefit));

    }

    @PostMapping("/memberBenefits/sync-status")
    @Operation(summary = "멤버 혜택 변경 Api",description = " 서비스 끼리 통신입니다. ",tags = "Service-MemberBenefit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<?> syncMemberBenefit(@RequestBody List<MemberBenefitReqDto.SyncMemberBenefit> syncMemberBenefitList){
        memberBenefitService.syncMemberBenefit(syncMemberBenefitList);
        return ApiResult.onSuccess();

    }

    @GetMapping("/memberBenefits/exist")
    @Operation(summary = "멤버 혜택 존재 여부 Api",description = " 서비스 끼리 통신입니다. ",tags = "Service-MemberBenefit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<Boolean> existMemberBenefit(@RequestParam Long benefitId){

        return ApiResult.onSuccess(memberBenefitService.existsMemberBenefit(benefitId));
    }


}

package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.dto.MemberBenefitReqDto;
import org.example.omnibeuser.service.MemberBenefitService;
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

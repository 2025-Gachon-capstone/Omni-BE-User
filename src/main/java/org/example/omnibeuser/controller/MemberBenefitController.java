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
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<List<Long>> createMemberBenefit(@RequestBody MemberBenefitReqDto.CreateMemberBenefit createMemberBenefitDto){

        return ApiResult.onSuccess(memberBenefitService.createMemberBenefit(createMemberBenefitDto));
    }


}

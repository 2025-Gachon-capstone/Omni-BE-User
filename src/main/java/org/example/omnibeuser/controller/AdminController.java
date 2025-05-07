package org.example.omnibeuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.omnibeuser.common.apiPayload.ApiResult;
import org.example.omnibeuser.common.apiPayload.code.status.ErrorStatus;
import org.example.omnibeuser.common.apiPayload.exception.GeneralException;
import org.example.omnibeuser.dto.MemberResDto;
import org.example.omnibeuser.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @DeleteMapping("/members/{memberId}")
    @Operation(summary = "사용자 삭제 API 입니다",description = "관리자 용입니다.. ( 인증 필요없음 )",tags = "Admin-Member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "COMMON200-성공",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4007", description = "MEMBER4007-이미 탈퇴한 사용자입니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "4008", description = "MEMBER4008-사용자를 찾지 못하였습니다.",content = @Content(schema = @Schema(implementation = ApiResult.class))),
    })
    public ApiResult<MemberResDto.DeleteMemberForAdmin> deleteMember(@Parameter(hidden = true) @RequestHeader("X-Authorization-Role") String role,
                                                  @PathVariable Long memberId) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        return ApiResult.onSuccess(adminService.deleteMemberForAdmin(memberId));

    }
}

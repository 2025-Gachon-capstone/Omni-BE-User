package org.example.omnibeuser.common.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.omnibeuser.common.apiPayload.code.BaseErrorCode;
import org.example.omnibeuser.common.apiPayload.code.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반 상태
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON500","서버 에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN,"COMMON402","금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON403","데이터를 찾지 못했습니다."),

    // member 상태
    _NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4001","비밀번호가 동일하지 않습니다."),
    _LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER4002", "로그인에 실패했습니다."),
    _ALREADY_EXIST_LOGINID(HttpStatus.BAD_REQUEST,"MEMBER4003","이미 존재하는 아이디입니다."),
    _NOT_FOUND_LOGINID(HttpStatus.NOT_FOUND,"MEMBER4004","없는 아이디입니다."),
    _NOT_MATCH_NEWPASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4005","새로운 비밀번호가 동일하지 않습니다."),
    _DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST,"MEMBER4006","기존 비밀번호와 동일합니다."),


    // token 상태
    _NOTFOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4001","리프레쉬 토큰이 없습니다."),
    _EXFIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4002","만료된 리프레쉬 토큰입니다."),
    _INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4003","유효하지 않은 리프레쉬 토큰입니다."),
    _LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"TOKEN5001","서버 오류 입니다. 다시 로그아웃 해주세요."),
    _NULL_REFRESH_TOKEN(HttpStatus.NOT_FOUND,"TOKEN4004","리프레쉬 토큰이 헤더에 없습니다."),
    _NULL_ACCESS_TOKEN(HttpStatus.NOT_FOUND,"TOKEN4005","엑세스 토큰이 헤더에 없습니다."),
    _EXFIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4006","만료된 엑세스 토큰입니다."),
    _INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4007","유효하지 않은 엑세스 토큰입니다."),
    _NOTFOUND_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN4008","엑세스 토큰이 없습니다."),

    // card 상태
    _CARD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"CARD4001","이미 존재하는 사용자입니다."),
    _CARD_NUMBER_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"CARD5002","중복된 카드 번호로 인한 생성 실패입니다."),
    _CARD_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"CARD5003","카드 생성 실패입니다."),


    // sponsor 상태
    _ALREADY_EXIST_SPONSOR(HttpStatus.BAD_REQUEST,"SPONSOR4001","이미 존재하는 스폰서입니다."),
    _SPONSOR_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"SPONSOR5001","스폰서 생성 실패입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}

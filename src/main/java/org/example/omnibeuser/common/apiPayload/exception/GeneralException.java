package org.example.omnibeuser.common.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.omnibeuser.common.apiPayload.code.BaseErrorCode;
import org.example.omnibeuser.common.apiPayload.code.ErrorReasonDto;


@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason(){
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }

}

package com.umc.authentication.apiPayload.exception;

import com.umc.authentication.apiPayload.code.BaseErrorCode;

public class MemberException extends GeneralException {
    public MemberException(BaseErrorCode code) {
        super(code);
    }
}

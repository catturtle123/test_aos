package com.umc.authentication.apiPayload.exception;


import com.umc.authentication.apiPayload.code.BaseErrorCode;

public class AuthException extends GeneralException {

    public AuthException(BaseErrorCode code) {
        super(code);
    }
}

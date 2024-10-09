package com.umc.authentication.security.exception;


import com.umc.authentication.apiPayload.BaseResponse;
import com.umc.authentication.security.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.umc.authentication.apiPayload.code.status.ErrorStatus.USER_AUTHENTICATION_FAIL;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        log.error("** JwtAuthenticationEntryPoint **");

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                USER_AUTHENTICATION_FAIL.getCode(),
                USER_AUTHENTICATION_FAIL.getMessage(),
                null);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
    }
}

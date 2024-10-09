package com.umc.authentication.security.filter;

import com.umc.authentication.apiPayload.BaseResponse;
import com.umc.authentication.apiPayload.code.status.ErrorStatus;
import com.umc.authentication.apiPayload.exception.AuthException;
import com.umc.authentication.security.util.HttpResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final String[] excludePath;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            log.error("[ERROR] : {}", e.getCode());

            ErrorStatus errorStatus = (ErrorStatus) e.getCode();

            BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null);

            HttpResponseUtil.setErrorResponse(response, e.getErrorReasonHttpStatus().getHttpStatus(), errorResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}

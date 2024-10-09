package com.umc.authentication.security.filter;


import com.umc.authentication.apiPayload.BaseResponse;
import com.umc.authentication.apiPayload.code.status.ErrorStatus;
import com.umc.authentication.apiPayload.exception.AuthException;
import com.umc.authentication.converter.MemberConverter;
import com.umc.authentication.dto.MemberRequestDTO;
import com.umc.authentication.repository.MemberRepository;
import com.umc.authentication.security.principal.PrincipalDetails;
import com.umc.authentication.security.util.HttpRequestUtil;
import com.umc.authentication.security.util.HttpResponseUtil;
import com.umc.authentication.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.umc.authentication.apiPayload.code.status.ErrorStatus.*;
import static com.umc.authentication.apiPayload.code.status.SuccessStatus._OK;

@Slf4j
@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response) throws AuthenticationException {

        MemberRequestDTO.LoginDTO loginDTO = HttpRequestUtil.readBody(request, MemberRequestDTO.LoginDTO.class);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getEmail(), loginDTO.getPassword());

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain,
            @NonNull Authentication authResult) throws IOException{
        SecurityContextHolder.getContext().setAuthentication(authResult);

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(principalDetails);

        HttpResponseUtil.setSuccessResponse(response, _OK, MemberConverter.toLoginResultDTO(principalDetails.getUserId(), accessToken));
    }

    @Override
    protected void unsuccessfulAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException failed) throws IOException {
        ErrorStatus errorStatus;

        if (failed instanceof UsernameNotFoundException) {
            errorStatus = USER_UNREGISTERED;
        } else if (failed instanceof BadCredentialsException) {
            errorStatus = BAD_CREDENTIALS;
        } else {
            errorStatus = AUTHENTICATION_FAILED;
        }
        log.error("[ERROR] : {}", errorStatus);

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
    }
}
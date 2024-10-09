package com.umc.authentication.security.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.authentication.apiPayload.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.umc.authentication.apiPayload.code.status.ErrorStatus.INVALID_REQUEST_BODY;
import static com.umc.authentication.apiPayload.code.status.ErrorStatus.INVALID_REQUEST_HEADER;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readBody(HttpServletRequest request, Class<T> requestDTO) {
        try {
            return objectMapper.readValue(request.getInputStream(), requestDTO);
        } catch (IOException e) {
            throw new AuthException(INVALID_REQUEST_BODY);
        }
    }

    public static String readHeader(HttpServletRequest request, String headerName) {
        String headerValue = request.getHeader(headerName);
        if (headerValue == null || headerValue.isEmpty()) {
            throw new AuthException(INVALID_REQUEST_HEADER);
        }
        return headerValue;
    }
}

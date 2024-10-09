package com.umc.authentication.dto;

import lombok.*;

import java.time.LocalDateTime;

public class MemberResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JoinResultDTO {
        private Long memberId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResultDTO {
        private Long memberId;
        private String accessToken;
    }
}

package com.umc.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class MemberRequestDTO {

    @Getter
    public static class JoinDTO {

        @Schema(description = "이름", example = "umc")
        @Length(min = 1, max = 30)
        @NotNull
        private String name;

        @Schema(description = "이메일 주소", example = "example@umc.com")
        @Email
        @NotNull
        @Length(min = 1, max = 30)
        private String email;

        @Schema(description = "비밀번호", example = "umc_password")
        @NotNull
        private String password;
    }

    @Getter
    public static class LoginDTO {
        @Schema(description = "이메일 주소", example = "example@umc.com")
        @Email
        @Length(min = 1, max = 30)
        private String email;

        @Schema(description = "비밀번호", example = "umc_password")
        @NotNull
        private String password;
    }
}

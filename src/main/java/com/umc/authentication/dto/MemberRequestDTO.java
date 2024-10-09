package com.umc.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class MemberRequestDTO {

    @Getter
    public static class JoinDTO {
        @Length(min = 1, max = 30)
        private String name;
        @Email
        @Length(min = 1, max = 30)
        private String email;
        @NotNull
        private String password;
    }

    @Getter
    public static class LoginDTO {
        @Email
        @Length(min = 1, max = 30)
        private String email;
        @NotNull
        private String password;
    }
}

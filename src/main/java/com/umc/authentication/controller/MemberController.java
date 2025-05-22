package com.umc.authentication.controller;

import com.umc.authentication.apiPayload.BaseResponse;
import com.umc.authentication.apiPayload.code.BaseErrorCode;
import com.umc.authentication.converter.MemberConverter;
import com.umc.authentication.dto.MemberRequestDTO;
import com.umc.authentication.dto.MemberResponseDTO;
import com.umc.authentication.entity.Member;
import com.umc.authentication.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member 및 test API", description = "Member 및 test 관련 Controller")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입 API", description = "새로운 사용자를 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                        다음과 같은 이유로 실패할 수 있습니다:
                        - AUTH_015: 이미 있는 유저입니다.
                        - 널이어서는 안 됩니다.
                        - 올바른 형식의 이메일 주소여야 합니다.
                        - 길이가 1에서 30 사이여야 합니다.
                    """,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PostMapping("/join")
    public BaseResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody @Valid MemberRequestDTO.JoinDTO joinDTO) {
        Member member = memberService.join(joinDTO);
        return BaseResponse.onSuccess(MemberConverter.toJoinResult(member));
    }

    @Operation(summary = "로그인 API", description = "회원 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        다음과 같은 이유로 실패할 수 있습니다:
                        - AUTH_014: 존재하지 않는 계정입니다. 회원가입을 진행해주세요.
                        - AUTH_008: 비밀번호를 잘못 입력했습니다.
                        - AUTH_009: 인증에 실패했습니다.
                    """,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PostMapping("/login")
    public BaseResponse<MemberResponseDTO.LoginResultDTO> login(@RequestBody @Valid MemberRequestDTO.LoginDTO loginDTO) {
        return null;
    }

    @Operation(summary = "테스트 API (JWT 함께 해주세요)", description = "API 테스트용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "테스트 성공", useReturnTypeSchema = true),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                        다음과 같은 이유로 인증에 실패할 수 있습니다:
                        - AUTH_001: 토큰이 만료되었습니다.
                        - AUTH_002: 토큰이 유효하지 않습니다.
                        - AUTH_003: 지원하지 않는 토큰 형식입니다.
                        - AUTH_004: 토큰 형식이 올바르지 않습니다.
                        - AUTH_005: 토큰 서명이 유효하지 않습니다.
                        - AUTH_006: 토큰이 비어있습니다.
                        - AUTH_007: 토큰 처리 중 오류가 발생했습니다.
                        - AUTH_010: 유저 인증에 실패했습니다.
                    """,
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )

    })
    @GetMapping("/test")
    public BaseResponse<String> test() {
        return BaseResponse.onSuccess("테스트 성공!");
    }
}

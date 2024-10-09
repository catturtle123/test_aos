package com.umc.authentication.apiPayload.code.status;


import com.umc.authentication.apiPayload.code.BaseErrorCode;
import com.umc.authentication.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 공통 에러
    PAGE_UNDER_ZERO(HttpStatus.BAD_REQUEST, "COMM_001", "페이지는 0이상이어야 합니다."),
    INVALID_SORT_CONDITION(HttpStatus.BAD_REQUEST, "COMM_002", "유효하지 않은 정렬 조건입니다."),

    // MEMBER 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "MEMBER가 존재하지 않습니다."),

    // AUTH 에러
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "토큰이 유효하지 않습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_003", "비밀번호를 잘못 입력했습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_004", "인증에 실패했습니다."),
    USER_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "AUTH_005", "유저 인증에 실패했습니다."),
    USER_INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "AUTH_006", "권한이 부족한 사용자 입니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "AUTH_007", "잘못된 요청 본문입니다."),
    INVALID_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "AUTH_008", "잘못된 요청 헤더입니다."),
    USER_UNREGISTERED(HttpStatus.UNAUTHORIZED, "AUTH_009", "존재하지 않는 계정입니다. 회원가입을 진행해주세요."),
    USER_ALREADY(HttpStatus.BAD_REQUEST, "AUTH_010", "이미 있는 유저입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}

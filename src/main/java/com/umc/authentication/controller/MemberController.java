package com.umc.authentication.controller;

import com.umc.authentication.apiPayload.BaseResponse;
import com.umc.authentication.converter.MemberConverter;
import com.umc.authentication.dto.MemberRequestDTO;
import com.umc.authentication.dto.MemberResponseDTO;
import com.umc.authentication.entity.Member;
import com.umc.authentication.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public BaseResponse<MemberResponseDTO.JoinResultDTO> join(@RequestBody MemberRequestDTO.JoinDTO joinDTO) {
        Member member = memberService.join(joinDTO);
        return BaseResponse.onSuccess(MemberConverter.toJoinResult(member));
    }

    @PostMapping("/login")
    public BaseResponse<MemberResponseDTO.LoginResultDTO> login(@RequestBody @Valid MemberRequestDTO.LoginDTO loginDTO) {
        return null;
    }

    @GetMapping("/test")
    public BaseResponse<String> test() {
        return BaseResponse.onSuccess("테스트 성공!");
    }
}

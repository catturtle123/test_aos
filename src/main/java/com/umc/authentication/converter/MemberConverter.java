package com.umc.authentication.converter;

import com.umc.authentication.dto.MemberRequestDTO;
import com.umc.authentication.dto.MemberResponseDTO;
import com.umc.authentication.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberConverter {

    public static Member toMember(MemberRequestDTO.JoinDTO joinDTO, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(joinDTO.getEmail())
                .name(joinDTO.getName())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .build();
    }

    public static MemberResponseDTO.JoinResultDTO toJoinResult(Member member) {
        return MemberResponseDTO.JoinResultDTO
                .builder()
                .memberId(member.getId())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();

    }

    public static MemberResponseDTO.LoginResultDTO toLoginResultDTO(Long memberId, String accessToken) {
        return MemberResponseDTO.LoginResultDTO
                .builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .build();

    }
}

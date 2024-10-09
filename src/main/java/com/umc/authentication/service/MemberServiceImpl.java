package com.umc.authentication.service;

import com.umc.authentication.apiPayload.exception.AuthException;
import com.umc.authentication.converter.MemberConverter;
import com.umc.authentication.dto.MemberRequestDTO;
import com.umc.authentication.entity.Member;
import com.umc.authentication.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.umc.authentication.apiPayload.code.status.ErrorStatus.USER_ALREADY;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member join(MemberRequestDTO.JoinDTO joinDTO) {

        if (memberRepository.findByEmail(joinDTO.getEmail()).isPresent()) {
            throw new AuthException(USER_ALREADY);
        }

        Member member = MemberConverter.toMember(joinDTO, passwordEncoder);

        memberRepository.save(member);

        return member;
    }

}

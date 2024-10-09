package com.umc.authentication.service;

import com.umc.authentication.dto.MemberRequestDTO;
import com.umc.authentication.entity.Member;

public interface MemberService {
    Member join(MemberRequestDTO.JoinDTO joinDTO);
}

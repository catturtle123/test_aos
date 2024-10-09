package com.umc.authentication.entity;

import com.umc.authentication.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", length = 30)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "name", length = 30)
    private String name;
}

package com.swyp.wedding.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tb_user")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;

    private String name;

    private LocalDate birth;

    private String phoneNumber;

    private String address;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserEnum auth;
}

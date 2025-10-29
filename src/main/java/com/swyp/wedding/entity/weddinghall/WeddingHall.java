package com.swyp.wedding.entity.weddinghall;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "tb_wedding_hall")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeddingHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WeddingHallEnum venueType;

    private int parking;

    private String address;

    private String phone;

    private String email;
}

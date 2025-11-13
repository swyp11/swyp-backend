package com.swyp.wedding.dto.weddinghall;

import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;
import lombok.Data;

@Data
public class WeddingHallRequest {
    private Long id;
    private String name;
    private WeddingHallEnum venueType;
    private int parking;
    private String address;
    private String phone;
    private String email;
    private String imageUrl;

    public WeddingHall toEntity() {
        return WeddingHall.builder()
                .name(name)
                .venueType(venueType)
                .parking(parking)
                .address(address)
                .phone(phone)
                .email(email)
                .imageUrl(imageUrl)
                .build();
    }
}

package com.swyp.wedding.dto.weddinghall;

import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WeddingHallResponse {
    private Long id;
    private String name;
    private WeddingHallEnum venueType;
    private int parking;
    private String address;
    private String phone;
    private String email;
    private String imageUrl;

    public static WeddingHallResponse from(WeddingHall weddingHall) {
        WeddingHallResponse response = new WeddingHallResponse();

        response.setId(weddingHall.getId());
        response.setName(weddingHall.getName());
        response.setVenueType(weddingHall.getVenueType());
        response.setParking(weddingHall.getParking());
        response.setAddress(weddingHall.getAddress());
        response.setPhone(weddingHall.getPhone());
        response.setEmail(weddingHall.getEmail());
        response.setImageUrl(weddingHall.getImageUrl());

        return response;
    }
}

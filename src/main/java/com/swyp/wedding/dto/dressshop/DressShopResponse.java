package com.swyp.wedding.dto.dressshop;

import lombok.Data;
import com.swyp.wedding.entity.dressshop.DressShop;

import java.time.LocalDateTime;

@Data
public class DressShopResponse {
    private Long id;
    private String shopName;        // 샵 이름
    private String description;     // 샵 설명
    private String address;         // 주소
    private String phone;           // 연락처 (전화번호, 카카오톡, 기타 연락 가능한 방법)
    private String snsUrl;          // SNS URL (웹사이트, 인스타그램 등)
    private String imageUrl;
    private String specialty;       // 전문분야
    private String features;        // 특징 또는 기타 정보
    private LocalDateTime regDt;
    private LocalDateTime updateDt;
    private Boolean isLiked;        // 로그인한 사용자의 찜 여부 (비로그인 시 null)

    public static DressShopResponse from(DressShop dressShop) {
        DressShopResponse response = new DressShopResponse();
        response.setId(dressShop.getId());
        response.setShopName(dressShop.getShopName());
        response.setDescription(dressShop.getDescription());
        response.setAddress(dressShop.getAddress());
        response.setPhone(dressShop.getPhone());
        response.setSnsUrl(dressShop.getSnsUrl());
        response.setImageUrl(dressShop.getImageUrl());
        response.setSpecialty(dressShop.getSpecialty());
        response.setFeatures(dressShop.getFeatures());
        response.setRegDt(dressShop.getRegDt());
        response.setUpdateDt(dressShop.getUpdateDt());
        return response;
    }
}
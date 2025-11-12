package com.swyp.wedding.dto.makeupshop;

import lombok.Data;
import com.swyp.wedding.entity.makeupshop.MakeupShop;

@Data
public class MakeupShopRequest {
    private Long id;
    private String shopName;        // 샵 이름
    private String description;     // 샵 설명
    private String address;         // 주소
    private String phone;           // 연락처 (전화번호, 카카오톡, 기타 연락 가능한 방법)
    private String snsUrl;          // SNS URL (웹사이트, 인스타그램 등)
    private String imageUrl;
    private String specialty;       // 전문분야
    private String features;        // 특징 또는 기타 정보

    public MakeupShop toEntity() {
        return MakeupShop.builder()
                .shopName(shopName)
                .description(description)
                .address(address)
                .phone(phone)
                .snsUrl(snsUrl)
                .imageUrl(imageUrl)
                .specialty(specialty)
                .features(features)
                .build();
    }
}

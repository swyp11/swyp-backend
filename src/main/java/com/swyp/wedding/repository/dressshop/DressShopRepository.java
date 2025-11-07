package com.swyp.wedding.repository.dressshop;

import com.swyp.wedding.entity.dressshop.DressShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DressShopRepository extends JpaRepository<DressShop, Long> {
    
    // 샵 이름으로 검색
    List<DressShop> findByShopNameContainingIgnoreCase(String shopName);
    
    // 지역(주소)으로 검색
    List<DressShop> findByAddressContainingIgnoreCase(String address);
    
    // 전문분야로 검색
    List<DressShop> findBySpecialtyContainingIgnoreCase(String specialty);
    
    // 등록일 기준 최신순으로 전체 드레스샵 조회
    List<DressShop> findAllByOrderByRegDtDesc();
}
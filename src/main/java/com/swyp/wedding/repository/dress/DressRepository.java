package com.swyp.wedding.repository.dress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.dress.Dress;

import java.util.List;

@Repository
public interface DressRepository extends JpaRepository<Dress, Long> {
    
    // shopName으로 드레스들 조회
    List<Dress> findByShopName(String shopName);
    
    // shopName으로 드레스들 조회 (부분 일치)
    List<Dress> findByShopNameContainingIgnoreCase(String shopName);
}
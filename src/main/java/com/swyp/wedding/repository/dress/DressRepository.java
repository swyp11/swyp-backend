package com.swyp.wedding.repository.dress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.dress.Dress;

import java.util.List;

@Repository
public interface DressRepository extends JpaRepository<Dress, Long> {

    // dressShopId로 드레스들 조회
    List<Dress> findByDressShopId(Long dressShopId);

    // dressShop의 shopName으로 드레스들 조회 (부분 일치)
    List<Dress> findByDressShop_ShopNameContainingIgnoreCase(String shopName);

    // 등록일 기준 최신순으로 전체 드레스 조회
    List<Dress> findAllByOrderByRegDtDesc();
}
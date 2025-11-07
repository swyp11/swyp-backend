package com.swyp.wedding.repository.dressshop;

import com.swyp.wedding.entity.dressshop.DressShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DressShopRepository extends JpaRepository<DressShop, Long> {
    
    // 등록일 기준 최신순으로 전체 드레스샵 조회
    List<DressShop> findAllByOrderByRegDtDesc();
}
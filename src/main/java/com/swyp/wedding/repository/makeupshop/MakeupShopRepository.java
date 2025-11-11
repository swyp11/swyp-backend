package com.swyp.wedding.repository.makeupshop;

import com.swyp.wedding.entity.makeupshop.MakeupShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakeupShopRepository extends JpaRepository<MakeupShop, Long> {
    
    // 등록일 기준 최신순으로 전체 메이크업샵 조회
    List<MakeupShop> findAllByOrderByRegDtDesc();
}

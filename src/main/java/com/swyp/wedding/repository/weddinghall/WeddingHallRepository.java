package com.swyp.wedding.repository.weddinghall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.weddinghall.WeddingHall;

@Repository
public interface WeddingHallRepository extends JpaRepository<WeddingHall, Long> {

    // 최신순 정렬
    List<WeddingHall> findAllByOrderByRegDtDesc();

    // 인기순 정렬 (찜 개수 기준)
    @Query("SELECT wh FROM WeddingHall wh " +
           "LEFT JOIN Likes l ON l.targetId = wh.id AND l.likesType = 'WEDDING_HALL' " +
           "GROUP BY wh.id " +
           "ORDER BY COUNT(l.id) DESC, wh.regDt DESC")
    List<WeddingHall> findAllOrderByLikesCountDesc();

    @Query("SELECT wh FROM WeddingHall wh " +
           "WHERE wh.name LIKE %:keyword%")
    List<WeddingHall> findByKeyword(String keyword);
}

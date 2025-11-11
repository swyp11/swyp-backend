package com.swyp.wedding.repository.likes;

import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    
    // SHOP 타입의 좋아요를 집계하여 target_id별 개수와 함께 반환 (많은 순서대로)
    @Query("SELECT l.targetId, COUNT(l) as likeCount " +
           "FROM Likes l " +
           "WHERE l.likesType = :likesType " +
           "GROUP BY l.targetId " +
           "ORDER BY likeCount DESC")
    List<Object[]> findTargetIdsByLikesTypeOrderByCountDesc(LikesType likesType);
}


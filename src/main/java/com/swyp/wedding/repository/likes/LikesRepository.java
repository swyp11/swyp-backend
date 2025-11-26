package com.swyp.wedding.repository.likes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.user.User;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    // SHOP 타입의 좋아요를 집계하여 target_id별 개수와 함께 반환 (많은 순서대로)
    @Query("SELECT l.targetId, COUNT(l) as likeCount " +
           "FROM Likes l " +
           "WHERE l.likesType = :likesType " +
           "GROUP BY l.targetId " +
           "ORDER BY likeCount DESC")
    List<Object[]> findTargetIdsByLikesTypeOrderByCountDesc(LikesType likesType);

    // 특정 사용자가 특정 게시물을 좋아요 했는지 확인
    boolean existsByUserAndLikesTypeAndTargetId(User user, LikesType likesType, Long targetId);

    // 특정 사용자의 여러 게시물에 대한 좋아요 목록 조회
    List<Likes> findByUserAndLikesTypeAndTargetIdIn(User user, LikesType likesType, List<Long> targetIds);

    // 특정 사용자의 모든 찜 목록 조회
    List<Likes> findByUserOrderByUpdateDtDesc(User user);

    // 특정 사용자의 카테고리별 찜 목록 조회
    List<Likes> findByUserAndLikesTypeOrderByUpdateDtDesc(User user, LikesType likesType);

    // 특정 사용자의 특정 게시물 찜 삭제
    void deleteByUserAndLikesTypeAndTargetId(User user, LikesType likesType, Long targetId);

    // 특정 사용자의 특정 게시물 찜 조회
    Optional<Likes> findByUserAndLikesTypeAndTargetId(User user, LikesType likesType, Long targetId);
}


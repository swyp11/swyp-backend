package com.swyp.wedding.repository.weddinghall;

import com.swyp.wedding.entity.weddinghall.WeddingHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingHallRepository extends JpaRepository<WeddingHall, Long> {
}

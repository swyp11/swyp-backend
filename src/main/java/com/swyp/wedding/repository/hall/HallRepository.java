package com.swyp.wedding.repository.hall;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.hall.Hall;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    List<Hall> findByWeddingHallId(Long weddingHallId);
}

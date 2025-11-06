package com.swyp.wedding.repository.hall;

import com.swyp.wedding.entity.hall.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
}

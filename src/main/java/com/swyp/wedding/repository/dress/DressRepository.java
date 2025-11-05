package com.swyp.wedding.repository.dress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.dress.Dress;

@Repository
public interface DressRepository extends JpaRepository<Dress, Long> {
}
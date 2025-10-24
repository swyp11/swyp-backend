package com.swyp.wedding.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

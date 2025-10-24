package com.swyp.wedding.repository.user;

import com.swyp.wedding.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserId(String userId);
    Boolean existsByPhoneNumber(String phoneNumber);

    User findByUserId (String userId);
}

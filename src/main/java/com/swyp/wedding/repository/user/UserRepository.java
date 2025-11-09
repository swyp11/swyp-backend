package com.swyp.wedding.repository.user;

import com.swyp.wedding.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserId(String userId);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional <User> findByUserId (String userId);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}

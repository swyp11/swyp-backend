package com.swyp.wedding.repository.user;

import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.entity.user.EmailAuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthCodeRepository extends JpaRepository<EmailAuthCode, Long> {

    Optional<EmailAuthCode> findByEmailAndPurpose(String email, AuthPurpose purpose);

    void deleteByEmailAndPurpose(String email, AuthPurpose purpose);

}

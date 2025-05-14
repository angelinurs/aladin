package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    Optional<EmailVerificationEntity> findTopByEmailAndTokenIgnoreCaseAndExpirationAtAfter(
            String email, String token, LocalDateTime now);
}

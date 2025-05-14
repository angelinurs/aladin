package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {

    Optional<LoginHistoryEntity> findTopByUserIdAndLoginIpAndUserAgentAndLogoutAtIsNullOrderByCreatedAtDesc(
            Long userId, String loginIp, String userAgent);
}

package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.JwtTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, Long> {

    // 사용자 ID로 JWT 토큰 정보 찾기
    Optional<JwtTokenEntity> findByUserIdAndUserAgentAndSecretKeyAndActivatedTrue(Long userId, String userAgent, String secretKey);

    List<JwtTokenEntity> findAllByUserIdOrderByIdDesc(Long userId);
}

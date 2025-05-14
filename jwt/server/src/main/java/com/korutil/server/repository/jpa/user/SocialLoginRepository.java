package com.korutil.server.repository.jpa.user;

import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.domain.user.SocialLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocialLoginRepository extends JpaRepository<SocialLoginEntity, Long> {
    Optional<SocialLoginEntity> findByProviderUserIdAndProvider(String providerUserId, OAuthProvider oAuthProvider);
    List<SocialLoginEntity> findByUserIdOrderByIdDesc(Long userId);
}

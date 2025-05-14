package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.UserCredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentialsEntity, Long> {

    // 사용자 ID로 사용자 자격 증명 정보 찾기
    Optional<UserCredentialsEntity> findByUserId(Long userId);
}

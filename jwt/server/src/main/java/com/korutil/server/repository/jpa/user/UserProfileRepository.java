package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.UserEntity;
import com.korutil.server.domain.user.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUser(UserEntity userEntity);
    Optional<UserProfileEntity> findByUserId(Long userId);
}

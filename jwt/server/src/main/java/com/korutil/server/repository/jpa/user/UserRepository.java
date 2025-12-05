package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 사용자 찾기
    Optional<UserEntity> findByEmail(String email);

    // 사용자 아이디로 찾기
    Optional<UserEntity> findByUsername(String username);

    // 이메일과 활성 상태가 true인 사용자 찾기
    List<UserEntity> findByEmailAndActivatedTrue(String email);

    boolean existsByEmail(String email);
}

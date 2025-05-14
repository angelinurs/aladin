package com.korutil.server.repository.jpa.user;

import com.korutil.server.domain.user.UserPasswordHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordHistoryRepository extends JpaRepository<UserPasswordHistoryEntity,Long> {
    boolean existsByUserIdAndPassword(Long userId, String password );
}

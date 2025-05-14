package com.korutil.server.dao.user;

import com.korutil.server.dto.user.UserCredentialsDto;
import com.korutil.server.domain.user.UserPasswordHistoryEntity;
import com.korutil.server.repository.jpa.user.UserPasswordHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserPasswordHistoryDao {

    private final UserPasswordHistoryRepository userPasswordHistoryRepository;

    @Transactional
    public boolean existsByUserIdAndPassword( Long userId, String newPassword ) {
        return userPasswordHistoryRepository.existsByUserIdAndPassword(userId, newPassword);
    }

    @Transactional
    public void save(UserCredentialsDto dto) {
        userPasswordHistoryRepository.save( UserPasswordHistoryEntity.fromDto(dto));
    }
}
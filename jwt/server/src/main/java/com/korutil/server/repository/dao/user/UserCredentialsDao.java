package com.korutil.server.repository.dao.user;

import com.korutil.server.dto.user.UserCredentialsDto;
import com.korutil.server.domain.user.UserCredentialsEntity;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserCredentialsDao {

    private final UserCredentialsRepository userCredentialsRepository;

    @Transactional
    public void createUserCredentials(UserCredentialsDto dto ) {
        userCredentialsRepository.save(UserCredentialsEntity.fromDto(dto));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public UserCredentialsDto getUserCredentials( Long userId ) {
        UserCredentialsEntity entity = userCredentialsRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_CREDENTIALS, "User credentials not found for userId: " + userId));
        return entity.toDto();
    }

    @Transactional
    public void save(UserCredentialsDto dto) {
        Long userId = dto.getUserId();
        UserCredentialsEntity entity = userCredentialsRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_CREDENTIALS, "User credentials not found for userId: " + userId));
        entity.setPassword(dto.getPassword());

        userCredentialsRepository.save( entity );
    }
}

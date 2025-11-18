package com.korutil.server.repository.dao.user;

import com.korutil.server.domain.user.UserEntity;
import com.korutil.server.domain.user.UserProfileEntity;
import com.korutil.server.dto.user.UserDto;
import com.korutil.server.dto.user.UserProfileDto;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class UserProfileDao {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void save(UserProfileDto dto) {
        userProfileRepository.save(UserProfileEntity.fromDto(dto));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public UserProfileDto findByUser(UserEntity userEntity) {
        UserProfileEntity entity = userProfileRepository.findByUser(userEntity).orElseThrow(
                () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER_PROFILE)
        );
        return entity.toDto();
    }
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public UserProfileDto findByUserId(Long userId) {
        UserProfileEntity entity = userProfileRepository.findByUserId(userId).orElseThrow(
                () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER_PROFILE)
        );
        return entity.toDto();
    }

    @Transactional
    public UserProfileDto update(Long id, UserProfileDto dto) {
        UserProfileEntity entity = userProfileRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER));
        entity.updateFromDto(dto);

        return entity.toDto();
    }
}

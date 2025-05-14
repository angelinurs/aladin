package com.korutil.server.dao.user;

import com.korutil.server.domain.user.UserProfileEntity;
import com.korutil.server.dto.user.UserAddressDto;
import com.korutil.server.domain.user.UserAddressEntity;
import com.korutil.server.dto.user.UserProfileDto;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserAddressDao {

    private final UserAddressRepository userAddressRepository;

    public void save(UserAddressDto dto) {
        userAddressRepository.save(UserAddressEntity.fromDto(dto));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<UserAddressDto> findAllUserAddressByUserId( Long userId ) {
        return
                Optional.ofNullable(userAddressRepository.findAllByUserIdAndActivatedTrueOrderBySeq(userId))
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(UserAddressEntity::toDto)
                        .toList();
    }

    @Transactional
    public UserAddressDto update(Long id, UserAddressDto dto) {
        UserAddressEntity entity = userAddressRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER));
        entity.updateFromDto(dto);

        return entity.toDto();
    }
}

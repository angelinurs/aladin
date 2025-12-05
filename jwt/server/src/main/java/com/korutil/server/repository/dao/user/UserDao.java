package com.korutil.server.repository.dao.user;

import com.korutil.server.dto.user.UserDto;
import com.korutil.server.domain.user.UserEntity;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDao {
    private final UserRepository userRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserEntity::toDto)
                .toList();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public UserDto getUser(Long id) {
        return userRepository.findById(id)
                .map(UserEntity::toDto)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserEntity::toDto)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public boolean isUserByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public UserEntity createUser(UserDto dto) {
        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomRuntimeException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        UserEntity entity = UserEntity.fromDto(dto);

        return userRepository.save(entity);
    }

    @Transactional
    public UserDto update(Long id, UserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_USER));

        entity.updateFromDto(dto);

        return entity.toDto();
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(
                UserEntity::softDelete,
                () -> { throw new CustomRuntimeException(ErrorCode.NOT_FOUND_USER); }
        );
    }

    public boolean existsByEmail(String email) {
        return
                userRepository.existsByEmail(email);
    }
}

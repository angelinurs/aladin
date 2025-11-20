package com.korutil.server.repository.dao.user;

import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.domain.user.JwtTokenEntity;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JwtTokenDao {

    private final JwtTokenRepository jwtTokenRepository;

    @Transactional(readOnly = true)
    public JwtTokenDto getRefreshToken( Long userId, String userAgent, String secretKey ) {

        JwtTokenEntity entity = jwtTokenRepository.findByUserIdAndUserAgentAndSecretKeyAndActivatedTrue( userId, userAgent, secretKey )
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_TOKEN));

        return entity.toDto();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToken( JwtTokenDto jwtTokenDto ) {
        List<JwtTokenEntity> entities = jwtTokenRepository.findAllByUserIdOrderByIdDesc( jwtTokenDto.getUserId() );
        if( entities.isEmpty() ) {
            throw new CustomRuntimeException(ErrorCode.NOT_FOUND_TOKEN);
        }

        JwtTokenEntity entity = entities.get(0);
        entity.updateToken(jwtTokenDto);
        jwtTokenRepository.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAccessToken( JwtTokenDto dto ) {
        jwtTokenRepository.save(JwtTokenEntity.fromDto(dto));
    }

    @Transactional(readOnly = true)
    public JwtTokenDto findByUserIdAndUserAgent( Long id, String userAgent, String secretKey ) {
        JwtTokenEntity entity = jwtTokenRepository.findByUserIdAndUserAgentAndSecretKeyAndActivatedTrue(id, userAgent, secretKey)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.NOT_FOUND_TOKEN));
        return entity.toDto();
    }

    @Transactional(readOnly = true)
    public JwtTokenDto findByUserId( Long userId ) {
        List<JwtTokenEntity> entities = jwtTokenRepository.findAllByUserIdOrderByIdDesc( userId );
        if( entities.isEmpty() ) {
            throw new CustomRuntimeException(ErrorCode.NOT_FOUND_TOKEN);
        }

        JwtTokenEntity entity = entities.get(0);
        return entity.toDto();
    }
}

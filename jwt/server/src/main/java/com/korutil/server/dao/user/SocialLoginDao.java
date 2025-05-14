package com.korutil.server.dao.user;

import com.korutil.server.domain.user.UserAddressEntity;
import com.korutil.server.dto.user.UserAddressDto;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.dto.user.SocialLoginDto;
import com.korutil.server.domain.user.SocialLoginEntity;
import com.korutil.server.repository.jpa.user.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class SocialLoginDao {
    private final SocialLoginRepository socialLoginRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public SocialLoginDto save(SocialLoginDto dto) {
        SocialLoginEntity entity = socialLoginRepository.save(SocialLoginEntity.fromDto(dto));
        return entity.toDto();
    }

    @Transactional(readOnly = true)
    public SocialLoginDto findByProviderUserIdAndOAuthProvider(String providerUserId, OAuthProvider oAuthProvider) {
        return
                socialLoginRepository.findByProviderUserIdAndProvider(providerUserId, oAuthProvider)
                        .map(SocialLoginEntity::toDto)
                        .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SocialLoginDto> findByUserIdOrderByIdDesc(Long userId) {
        return
                socialLoginRepository.findByUserIdOrderByIdDesc(userId)
                        .stream()
                        .map(SocialLoginEntity::toDto)
                        .toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Long userId) {
        List<SocialLoginEntity> entities = socialLoginRepository.findByUserIdOrderByIdDesc(userId);
        entities.forEach(SocialLoginEntity::deActivated);
    }
}

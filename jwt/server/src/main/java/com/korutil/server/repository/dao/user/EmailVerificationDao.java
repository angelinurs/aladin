package com.korutil.server.repository.dao.user;

import com.korutil.server.dto.user.EmailVerificationDto;
import com.korutil.server.domain.user.EmailVerificationEntity;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.EmailVerificationRepository;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class EmailVerificationDao {

    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(EmailVerificationDto dto) {
        emailVerificationRepository.save(EmailVerificationEntity.fromDto(dto));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(EmailVerificationDto dto) {

        EmailVerificationEntity emailVerificationEntity =
                emailVerificationRepository.findTopByEmailAndTokenIgnoreCaseAndExpirationAtAfter( dto.getEmail(), dto.getToken(), CustomLocalDateTimeUtils.getNow()).orElseThrow(
                        () -> new CustomRuntimeException(ErrorCode.NOT_FOUND_EMAIL_VERIFICATION));

        emailVerificationEntity.update();
        emailVerificationRepository.save(emailVerificationEntity);
    }
}

package com.korutil.server.repository.dao.user;

import com.korutil.server.dto.user.LoginHistoryDto;
import com.korutil.server.domain.user.LoginHistoryEntity;
import com.korutil.server.repository.jpa.user.LoginHistoryRepository;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginHistoryDao {

    private final LoginHistoryRepository loginHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLogout(LoginHistoryDto loginHistoryDto)  {

        // 기존에 로그아웃되지 않은 로그인 기록을 찾기
        loginHistoryRepository.findTopByUserIdAndLoginIpAndUserAgentAndLogoutAtIsNullOrderByCreatedAtDesc(
                        loginHistoryDto.getUserId(), loginHistoryDto.getLoginIp(), loginHistoryDto.getUserAgent())
                .ifPresentOrElse(
                        entity -> {
                            // 기존 로그인 기록이 있으면 로그아웃 시간 기록
                            entity.setLogoutAt(CustomLocalDateTimeUtils.getNow());
                            loginHistoryRepository.save(entity);
                        },
                        () -> log.warn("로그아웃할 기록을 찾을 수 없습니다: {} - {} - {}",
                                loginHistoryDto.getUserId(), loginHistoryDto.getLoginIp(), loginHistoryDto.getUserAgent())
                );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLogin(LoginHistoryDto loginHistoryDto)  {
        loginHistoryRepository.save(LoginHistoryEntity.fromDto(loginHistoryDto));
    }
}

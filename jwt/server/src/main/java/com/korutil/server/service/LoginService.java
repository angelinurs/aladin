package com.korutil.server.service;

import com.korutil.server.repository.dao.user.LoginHistoryDao;
import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.dto.user.LoginHistoryDto;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import com.korutil.server.util.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginHistoryDao loginHistoryDao;

    // login
    public void addLoginHistory(JwtTokenDto jwtTokenDto)   {
        LoginHistoryDto dto =
                LoginHistoryDto.builder()
                    .userId(jwtTokenDto.getUserId())
                    .userAgent(jwtTokenDto.getUserAgent())
                    .loginIp(RequestContext.getClientIp())
                    .build();
        loginHistoryDao.saveLogin( dto );
    }

    // logout
    public void addLogoutHistory(JwtTokenDto jwtTokenDto)  {
        LoginHistoryDto dto =
                LoginHistoryDto.builder()
                        .userId(jwtTokenDto.getUserId())
                        .userAgent(jwtTokenDto.getUserAgent())
                        .loginIp(RequestContext.getClientIp())
                        .build();
        dto.setLogoutAt(CustomLocalDateTimeUtils.getNow());
        loginHistoryDao.saveLogout(dto);
    }
}

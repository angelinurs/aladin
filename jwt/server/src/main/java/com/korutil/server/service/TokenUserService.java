package com.korutil.server.service;

import com.korutil.server.repository.dao.user.UserAddressDao;
import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.user.record.UserAddressResponse;
import com.korutil.server.service.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TokenUserService {

    private final UserAddressDao userAddressDao;
    private final JwtTokenService jwtTokenService;

    public CommonApiResponse<List<UserAddressResponse>> findAllUserAddress() {
        Long userId = jwtTokenService.getUserIdFromToken();

        List<UserAddressResponse> responses =
                userAddressDao.findAllUserAddressByUserId(userId)
                        .stream()
                        .map(UserAddressResponse::fromDto)
                        .toList();

        return CommonApiResponse.success( responses );
    }
}

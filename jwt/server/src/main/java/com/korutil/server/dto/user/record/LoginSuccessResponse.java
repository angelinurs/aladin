package com.korutil.server.dto.user.record;

import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.dto.user.UserDto;

public record LoginSuccessResponse(
        String accessToken,
        String refreshToken,
        UserSimpleResponse user
) {
    public static LoginSuccessResponse from(UserDto userDto, JwtTokenDto jwtTokenDto) {
        UserSimpleResponse user = UserSimpleResponse.fromDto(userDto);
        return new LoginSuccessResponse(
                jwtTokenDto.getAccessToken(),
                jwtTokenDto.getRefreshToken(),
                user
                );
    }
}

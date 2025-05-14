package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "이용자 정보")
public record MemberResponse(
        UserSimpleResponse user,
        UserProfileResponse profile,
        UserAddressResponse address
) {
    public static MemberResponse ofDto( MemberDto dto ) {

        UserAddressResponse address = dto.getUserAddressDtos().stream()
                .findFirst()
                .map(UserAddressResponse::fromDto)
                .orElse(null); // Explicitly handle empty case

        return new MemberResponse(
                UserSimpleResponse.fromDto(dto.getUserDto()),
                UserProfileResponse.fromDto(dto.getUserProfileDto()),
                address );
    }
}

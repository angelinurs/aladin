package com.korutil.server.dto.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private Long userId;

    private UserDto userDto;
    private UserProfileDto userProfileDto;
    private List<UserAddressDto> userAddressDtos;
}

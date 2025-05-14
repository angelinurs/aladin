package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.MemberDto;
import com.korutil.server.dto.user.UserAddressDto;
import com.korutil.server.dto.user.UserDto;
import com.korutil.server.dto.user.UserProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "이용자 정보 수정")
public record MemberRequest(

        @Schema(description = "로그인에 사용되는 유저네임", example = "naru")
        String username,  // 이름 또는 닉네임

        @Schema(description = "로그인에 사용되는 유저네임", example = "naru@naru.com")
        String email,  // 이메일

        @Schema(description = "이름 또는 닉네임", example = "나루")
        String name,

        @Schema(description = "전화번호", example = "01012345678")
        String phone,

        @Schema(description = "생년월일", type = "string", format = "date", example = "1990-01-01")
        LocalDate birth,

        // 주소 관련 필드
        @Schema(description = "주소 관련 필드")
        UserAddressRequest address,

        @Schema(description = "이메일 인증 여부", example = "false", defaultValue = "false")
        Boolean emailVerified
) {

    // DTO로 변환하는 메서드
    public UserDto toUserDto() {
        return UserDto.builder()
                .username(username)
                .email(email)
                .emailVerified(emailVerified)
                .build();
    }

    public UserAddressDto toUserAddressDto() {
        UserAddressDto dto = address.toDto();
        dto.setSeq(0);
        dto.setActivated(true);

        return dto;
    }

    public UserProfileDto toUserProfileDto() {
        return
                UserProfileDto.builder()
                        .name(name)
                        .phone(phone)
                        .birth(birth)
                        .activated(true)
                        .build();
    }

    public MemberDto toMemberDto() {
        return
                MemberDto.builder()
                        .userDto(toUserDto())
                        .userProfileDto(toUserProfileDto())
                        .userAddressDtos(List.of(toUserAddressDto()))
                        .build();
    }
}

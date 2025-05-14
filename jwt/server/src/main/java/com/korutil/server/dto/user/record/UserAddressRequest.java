package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.UserAddressDto;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "사용자 주소 응답 객체")
public record UserAddressRequest(
        @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
        String roadAddress,

        @Schema(description = "지번 주소", example = "강남동 456-7")
        String jibunAddress,

        @Schema(description = "상세주소", example = "101동 202호")
        String detailAddress,

        @Schema(description = "참고항목", example = "삼성아파트")
        String extraAddress,

        @Schema(description = "우편번호", example = "06236")
        String postcode
) {
    public UserAddressDto toDto() {
        return
                UserAddressDto.builder()
                        .roadAddress(roadAddress)
                        .jibunAddress(jibunAddress)
                        .detailAddress(detailAddress)
                        .extraAddress(extraAddress)
                        .postcode(postcode)
                        .build();
    }
}

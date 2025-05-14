package com.korutil.server.dto.todo.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.todo.TodoSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.ZoneId;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Todo 검색 요청 객체")
public record TodoSearchRequest(
        @Schema(description = "제목 또는 설명에 포함된 키워드", example = "회의" )
        String keyword,

        @Schema(description = "할 일의 카테고리", example = "업무")
        String category,
        @Schema(description = "완료 여부 (null일 경우 전체 조회)", example = "false")
        Boolean completed,

        @Schema(description = "시작 일시 범위의 시작점", example = "2025-05-20T09:30")
        LocalDateTime startFrom,
        @Schema(description = "시작 일시 범위의 종료점", example = "2025-05-20T18:00")
        LocalDateTime startTo,
        @Schema(description = "종료 일시 범위의 시작점", example = "2025-05-20T09:30")
        LocalDateTime endFrom,
        @Schema(description = "종료 일시 범위의 종료점", example = "2025-05-20T18:00")
        LocalDateTime endTo,

        @Schema(description = "종일 이벤트 여부 (null일 경우 전체 조회)", example = "false")
        Boolean allDay,
        @Schema(description = "활성화 여부 (null일 경우 전체 조회)", example = "true")
        Boolean activated
) {
    public TodoSearchDto toDto() {
        ZoneId utcZone = ZoneId.of("UTC");
        return
                TodoSearchDto.builder()
                        .keyword(keyword)
                        .category(category)
                        .completed(completed)
                        .startFrom(startFrom.atZone(utcZone).toLocalDateTime())
                        .startTo(startTo.atZone(utcZone).toLocalDateTime())
                        .endFrom(endFrom.atZone(utcZone).toLocalDateTime())
                        .endTo(endTo.atZone(utcZone).toLocalDateTime())
                        .allDay(allDay)
                        .activated(activated)
                        .build();
    }
}

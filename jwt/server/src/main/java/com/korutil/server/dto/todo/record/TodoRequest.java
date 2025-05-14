package com.korutil.server.dto.todo.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.todo.TodoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Todo 요청 객체")
public record TodoRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Schema(description = "할 일 제목", example = "회의 준비")
        String title,

//        @NotBlank(message = "카테고리는 필수입니다.")
        @Schema(description = "카테고리", example = "업무")
        String category,

//        @NotNull(message = "시작 일시는 필수입니다.")
        @Schema(description = "시작 일시", example = "2025-05-20T10:00:00")
        LocalDateTime startDate,

//        @NotNull(message = "종료 일시는 필수입니다.")
        @Schema(description = "종료 일시", example = "2025-05-20T12:00:00")
        LocalDateTime endDate,

        @Schema(description = "상세 설명", example = "회의실 3층")
        String description,

        @Schema(description = "종일 여부", example = "false")
        boolean allDay,

        @Schema(description = "완료 여부", example = "false")
        boolean completed
) {
    public TodoDto toDto() {
        return TodoDto.builder()
                .title(this.title)
                .category(this.category)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .description(this.description)
                .allDay(this.allDay)
                .completed(this.completed)
                .build();
    }
}
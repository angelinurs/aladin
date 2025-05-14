package com.korutil.server.dto.todo.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.todo.TodoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Todo 응답 객체")
public record TodoResponse(
        Long id,
        Long userId,
        String title,
        String category,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String description,
        boolean allDay,
        boolean completed,
        boolean activated,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TodoResponse fromDto(TodoDto dto) {
        return
                new TodoResponse(
                        dto.getId(),
                        dto.getUserId(),
                        dto.getTitle(),
                        dto.getCategory(),
                        dto.getStartDate(),
                        dto.getEndDate(),
                        dto.getDescription(),
                        dto.getAllDay(),
                        dto.getCompleted(),
                        dto.getActivated(),
                        dto.getDeletedAt(),
                        dto.getCreatedAt(),
                        dto.getUpdatedAt()
                );
    }
}

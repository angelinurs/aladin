package com.korutil.server.dto.todo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDto {
    private Long id;
    private Long userId;
    private String title;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private Boolean allDay;
    private Boolean completed;

    private Boolean activated;
    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

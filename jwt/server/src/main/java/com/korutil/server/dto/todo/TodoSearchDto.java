package com.korutil.server.dto.todo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoSearchDto {
    private Long userId;
    private String keyword;
    private String category;
    private Boolean completed;
    private LocalDateTime startFrom;
    private LocalDateTime startTo;
    private LocalDateTime endFrom;
    private LocalDateTime endTo;
    private Boolean allDay;
    private Boolean activated;
}

package com.korutil.server.domain.user;

import com.korutil.server.domain.usecase.AdvancedBaseEntity;
import com.korutil.server.dto.todo.TodoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo", schema = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodoEntity extends AdvancedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id", nullable = false)
    protected Long userId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String category;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(length = 500)
    private String description;

    @Column(name = "all_day", nullable = false)
    @Builder.Default
    private boolean allDay = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean completed = false;

    public void updateFromDto(TodoDto dto) {
        if(!title.equals(dto.getTitle())) {
            title = dto.getTitle();
        }

        if(!category.equals(dto.getCategory())) {
            category = dto.getCategory();
        }

        if(!startDate.equals(dto.getStartDate())) {
            startDate = dto.getStartDate();
        }

        if(!endDate.equals(dto.getEndDate())) {
            endDate = dto.getEndDate();
        }

        if(!description.equals(dto.getDescription())) {
            description = dto.getDescription();
        }

        if(allDay ^ dto.getAllDay()) {
            allDay = dto.getAllDay();
        }

        if(!completed ^ dto.getCompleted()) {
            completed = dto.getCompleted();
        }
    }

    public TodoDto toDto() {
        return
                TodoDto.builder()
                        .id(id)
                        .userId(userId)
                        .title(title)
                        .category(category)
                        .startDate(startDate)
                        .endDate(endDate)
                        .description(description)
                        .allDay(allDay)
                        .completed(completed)
                        .activated(activated)
                        .deletedAt(deletedAt)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .build();
    }

    public static TodoEntity fromDto(TodoDto dto)  {
        return
                TodoEntity.builder()
                        .userId(dto.getUserId())
                        .title(dto.getTitle())
                        .category(dto.getCategory())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .description(dto.getDescription())
                        .allDay(dto.getAllDay())
                        .completed(dto.getCompleted())
                        .build();
    }
}

package com.korutil.server.dto.todo.record;

import java.util.List;

public record TodoSearchResponse(
        List<TodoResponse> todos,
        int currentPage,
        int size,
        long totalElements,
        int totalPages
) {}
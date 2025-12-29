package com.example.eduplatform.dto;

import java.util.List;

public record ModuleResponse(
        Long id,
        String title,
        int orderIndex,
        String description,
        List<LessonResponse> lessons
) {}

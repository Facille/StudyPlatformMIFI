package com.example.eduplatform.dto;

import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String description,
        Long categoryId,
        Long teacherId,
        List<ModuleResponse> modules
) {}

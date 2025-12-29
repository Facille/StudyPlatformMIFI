package com.example.eduplatform.dto;

import jakarta.validation.constraints.Size;

public record UpdateCourseRequest(
        @Size(min = 1, max = 255) String title,
        String description,
        Long categoryId,
        Long teacherId
) {}

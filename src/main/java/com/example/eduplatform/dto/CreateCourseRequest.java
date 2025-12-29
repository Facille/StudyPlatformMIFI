package com.example.eduplatform.dto;

import jakarta.validation.constraints.*;

public record CreateCourseRequest(
        @NotBlank String title,
        String description,
        @NotNull Long categoryId,
        @NotNull Long teacherId
) {}

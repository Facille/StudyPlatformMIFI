package com.example.eduplatform.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLessonRequest(
        @NotBlank String title,
        String content,
        String videoUrl
) {}

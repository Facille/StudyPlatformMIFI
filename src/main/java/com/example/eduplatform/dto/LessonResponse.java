package com.example.eduplatform.dto;

public record LessonResponse(
        Long id,
        String title,
        String content,
        String videoUrl
) {}

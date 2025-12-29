package com.example.eduplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateAssignmentRequest(
        @NotNull Long lessonId,
        @NotBlank String title,
        String description,
        LocalDate dueDate,
        Integer maxScore
) {}

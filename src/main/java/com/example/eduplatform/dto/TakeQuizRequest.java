package com.example.eduplatform.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record TakeQuizRequest(
        @NotNull Long studentId,
        @NotNull Long quizId,
        @NotNull Map<Long, Long> answers // questionId -> selectedOptionId
) {}

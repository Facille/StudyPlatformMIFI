package com.example.eduplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerOptionRequest(
        @NotNull Long questionId,
        @NotBlank String text,
        boolean isCorrect
) {}

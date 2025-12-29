package com.example.eduplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQuizRequest(
        @NotNull Long moduleId,
        @NotBlank String title,
        Integer timeLimit
) {}

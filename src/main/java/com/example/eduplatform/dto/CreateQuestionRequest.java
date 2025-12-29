package com.example.eduplatform.dto;

import com.example.eduplatform.entity.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQuestionRequest(
        @NotNull Long quizId,
        @NotBlank String text,
        @NotNull QuestionType type
) {}

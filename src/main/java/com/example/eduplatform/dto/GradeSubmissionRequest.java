package com.example.eduplatform.dto;

import jakarta.validation.constraints.NotNull;

public record GradeSubmissionRequest(
        @NotNull Integer score,
        String feedback
) {}

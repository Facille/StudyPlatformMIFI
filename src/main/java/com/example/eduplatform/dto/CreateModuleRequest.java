package com.example.eduplatform.dto;

import jakarta.validation.constraints.*;

public record CreateModuleRequest(
        @NotBlank String title,
        @Min(1) int orderIndex,
        String description
) {}

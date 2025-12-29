package com.example.eduplatform.dto;

import com.example.eduplatform.entity.EnrollmentStatus;

import java.time.LocalDate;

public record EnrollmentResponse(
        Long id,
        Long courseId,
        Long studentId,
        LocalDate enrollDate,
        EnrollmentStatus status
) {}

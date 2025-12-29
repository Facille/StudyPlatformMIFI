package com.example.eduplatform.controller;

import com.example.eduplatform.dto.EnrollmentResponse;
import com.example.eduplatform.dto.EnrollRequest;
import com.example.eduplatform.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll(@PathVariable Long courseId, @RequestBody @Valid EnrollRequest req) {
        return enrollmentService.enroll(courseId, req);
    }

    @DeleteMapping("/courses/{courseId}/enrollments/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long courseId, @PathVariable Long studentId) {
        enrollmentService.cancel(courseId, studentId);
    }

    @GetMapping("/courses/{courseId}/enrollments")
    public List<EnrollmentResponse> listByCourse(@PathVariable Long courseId) {
        return enrollmentService.listByCourse(courseId);
    }

    @GetMapping("/students/{studentId}/enrollments")
    public List<EnrollmentResponse> listByStudent(@PathVariable Long studentId) {
        return enrollmentService.listByStudent(studentId);
    }
}

package com.example.eduplatform.controller;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.Assignment;
import com.example.eduplatform.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping("/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse create(@RequestBody @Valid CreateAssignmentRequest req) {
        return new IdResponse(assignmentService.createAssignment(req).getId());
    }

    @PostMapping("/submissions")
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionResponse submit(@RequestBody @Valid SubmitAssignmentRequest req) {
        return assignmentService.submit(req);
    }

    @PatchMapping("/submissions/{id}/grade")
    public SubmissionResponse grade(@PathVariable Long id, @RequestBody @Valid GradeSubmissionRequest req) {
        return assignmentService.grade(id, req);
    }

    @GetMapping("/submissions/by-assignment/{assignmentId}")
    public List<SubmissionResponse> byAssignment(@PathVariable Long assignmentId) {
        return assignmentService.submissionsByAssignment(assignmentId);
    }

    @GetMapping("/submissions/by-student/{studentId}")
    public List<SubmissionResponse> byStudent(@PathVariable Long studentId) {
        return assignmentService.submissionsByStudent(studentId);
    }
}

package com.example.eduplatform.service;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.exception.BadRequestException;
import com.example.eduplatform.exception.NotFoundException;
import com.example.eduplatform.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    public Assignment createAssignment(CreateAssignmentRequest req) {
        Lesson lesson = lessonRepository.findById(req.lessonId())
                .orElseThrow(() -> new NotFoundException("Lesson not found: " + req.lessonId()));

        Assignment a = Assignment.builder()
                .lesson(lesson)
                .title(req.title())
                .description(req.description())
                .dueDate(req.dueDate())
                .maxScore(req.maxScore())
                .build();

        return assignmentRepository.save(a);
    }

    @Transactional
    public SubmissionResponse submit(SubmitAssignmentRequest req) {
        User student = userRepository.findById(req.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found: " + req.studentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new BadRequestException("User is not a STUDENT: " + req.studentId());
        }

        Assignment assignment = assignmentRepository.findById(req.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment not found: " + req.assignmentId()));

        submissionRepository.findByStudentIdAndAssignmentId(req.studentId(), req.assignmentId())
                .ifPresent(s -> { throw new BadRequestException("Submission already exists"); });

        Submission s = Submission.builder()
                .student(student)
                .assignment(assignment)
                .submittedAt(LocalDateTime.now())
                .content(req.content())
                .build();

        return toResponse(submissionRepository.save(s));
    }

    @Transactional
    public SubmissionResponse grade(Long submissionId, GradeSubmissionRequest req) {
        Submission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new NotFoundException("Submission not found: " + submissionId));

        s.setScore(req.score());
        s.setFeedback(req.feedback());

        return toResponse(submissionRepository.save(s));
    }

    @Transactional
    public List<SubmissionResponse> submissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public List<SubmissionResponse> submissionsByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    private SubmissionResponse toResponse(Submission s) {
        return new SubmissionResponse(
                s.getId(),
                s.getAssignment().getId(),
                s.getStudent().getId(),
                s.getSubmittedAt(),
                s.getContent(),
                s.getScore(),
                s.getFeedback()
        );
    }
}

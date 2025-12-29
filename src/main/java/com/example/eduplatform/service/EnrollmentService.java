package com.example.eduplatform.service;

import com.example.eduplatform.dto.EnrollmentResponse;
import com.example.eduplatform.dto.EnrollRequest;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.exception.BadRequestException;
import com.example.eduplatform.exception.NotFoundException;
import com.example.eduplatform.repository.CourseRepository;
import com.example.eduplatform.repository.EnrollmentRepository;
import com.example.eduplatform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public EnrollmentResponse enroll(Long courseId, EnrollRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        User student = userRepository.findById(req.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found: " + req.studentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new BadRequestException("User is not a STUDENT: " + student.getId());
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new BadRequestException("Student already enrolled: " + student.getId() + " to course " + course.getId());
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();

        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public void cancel(Long courseId, Long studentId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new NotFoundException("Enrollment not found for student " + studentId + " and course " + courseId));

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
    }

    @Transactional
    public List<EnrollmentResponse> listByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public List<EnrollmentResponse> listByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void unenroll(Long courseId, Long studentId) {
        Enrollment e = enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new NotFoundException("Enrollment not found"));

        // вариант 1: удаляем запись
        enrollmentRepository.delete(e);

    }

    @Transactional
    public List<EnrollmentResponse> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findAllByStudentId(studentId).stream()
                .map(this::toResponse)
                .toList();
    }


    private EnrollmentResponse toResponse(Enrollment e) {
        return new EnrollmentResponse(
                e.getId(),
                e.getCourse().getId(),
                e.getStudent().getId(),
                e.getEnrollDate(),
                e.getStatus()
        );
    }
}

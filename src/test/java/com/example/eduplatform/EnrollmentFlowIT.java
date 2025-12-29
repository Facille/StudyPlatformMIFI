package com.example.eduplatform;

import com.example.eduplatform.dto.CreateCourseRequest;
import com.example.eduplatform.dto.EnrollRequest;
import com.example.eduplatform.dto.EnrollmentResponse;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.exception.BadRequestException;
import com.example.eduplatform.repository.CategoryRepository;
import com.example.eduplatform.repository.UserRepository;
import com.example.eduplatform.service.CourseService;
import com.example.eduplatform.service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class EnrollmentFlowIT {

    @Autowired EnrollmentService enrollmentService;
    @Autowired CourseService courseService;

    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;

    @Test
    void enroll_unenroll_and_prevent_duplicates() {
        // given
        Category cat = categoryRepository.save(Category.builder().name("DB").build());

        User teacher = userRepository.save(
                User.builder().name("T").email("t@x.com").role(Role.TEACHER).build()
        );
        User student = userRepository.save(
                User.builder().name("S").email("s@x.com").role(Role.STUDENT).build()
        );

        var course = courseService.createCourse(
                new CreateCourseRequest("SQL", "Intro", cat.getId(), teacher.getId())
        );

        // when: enroll
        EnrollmentResponse e1 = enrollmentService.enroll(
                course.id(),
                new EnrollRequest(student.getId())
        );

        // then
        assertThat(e1.studentId()).isEqualTo(student.getId());
        assertThat(e1.courseId()).isEqualTo(course.id());
        assertThat(e1.status()).isEqualTo(EnrollmentStatus.ACTIVE);

        // when/then: duplicate -> BAD_REQUEST
        assertThatThrownBy(() -> enrollmentService.enroll(course.id(), new EnrollRequest(student.getId())))
                .isInstanceOf(BadRequestException.class);

        // when: unenroll
        enrollmentService.unenroll(course.id(), student.getId());

        // then: student has no enrollments
        var list = enrollmentService.getStudentEnrollments(student.getId());
        assertThat(list).isEmpty();
    }
}

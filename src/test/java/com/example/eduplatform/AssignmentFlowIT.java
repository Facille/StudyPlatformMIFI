package com.example.eduplatform;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.repository.CategoryRepository;
import com.example.eduplatform.repository.UserRepository;
import com.example.eduplatform.service.AssignmentService;
import com.example.eduplatform.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AssignmentFlowIT {

    @Autowired CourseService courseService;
    @Autowired AssignmentService assignmentService;
    @Autowired CategoryRepository categoryRepository;
    @Autowired UserRepository userRepository;

    @Test
    void createAssignment_submit_once_and_grade() {
        var cat = categoryRepository.save(Category.builder().name("Java").build());
        var teacher = userRepository.save(User.builder().name("T").email("t2@x.com").role(Role.TEACHER).build());
        var student = userRepository.save(User.builder().name("S").email("s2@x.com").role(Role.STUDENT).build());

        var course = courseService.createCourse(new CreateCourseRequest("Hibernate", "Intro", cat.getId(), teacher.getId()));
        var module = courseService.addModule(course.id(), new CreateModuleRequest("M1", 1, null));
        var lesson = courseService.addLesson(module.id(), new CreateLessonRequest("L1", "content", null));

        var assignment = assignmentService.createAssignment(new CreateAssignmentRequest(
                lesson.id(), "HW1", "Do it", null, 100
        ));

        var sub = assignmentService.submit(new SubmitAssignmentRequest(student.getId(), assignment.getId(), "my answer"));
        assertThat(sub.score()).isNull();

        assertThatThrownBy(() -> assignmentService.submit(new SubmitAssignmentRequest(student.getId(), assignment.getId(), "again")))
                .isInstanceOf(RuntimeException.class);

        var graded = assignmentService.grade(sub.id(), new GradeSubmissionRequest(95, "good"));
        assertThat(graded.score()).isEqualTo(95);
    }
}

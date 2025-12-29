package com.example.eduplatform;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.repository.CategoryRepository;
import com.example.eduplatform.repository.UserRepository;
import com.example.eduplatform.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CourseFlowIT {

    @Autowired CourseService courseService;
    @Autowired CategoryRepository categoryRepository;
    @Autowired UserRepository userRepository;

    @Test
    void createCourse_addModule_addLesson_flowWorks() {
        Category cat = categoryRepository.save(Category.builder().name("Programming").build());
        User teacher = userRepository.save(User.builder().name("Teach").email("teach@mail.com").role(Role.TEACHER).build());

        CourseResponse course = courseService.createCourse(new CreateCourseRequest(
                "Hibernate Basics", "Intro", cat.getId(), teacher.getId()
        ));

        ModuleResponse module = courseService.addModule(course.id(), new CreateModuleRequest(
                "Module 1", 1, "ORM intro"
        ));

        LessonResponse lesson = courseService.addLesson(module.id(), new CreateLessonRequest(
                "Lesson 1", "Content", null
        ));

        CourseResponse loaded = courseService.getCourse(course.id());

        assertThat(loaded.modules()).hasSize(1);
        assertThat(loaded.modules().get(0).lessons()).hasSize(1);
        assertThat(loaded.modules().get(0).lessons().get(0).id()).isEqualTo(lesson.id());
    }
}

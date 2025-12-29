package com.example.eduplatform;

import com.example.eduplatform.entity.*;
import com.example.eduplatform.repository.CategoryRepository;
import com.example.eduplatform.repository.CourseRepository;
import com.example.eduplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.example.eduplatform.entity.Role;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EnrollmentApiIT {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired CourseRepository courseRepository;

    private Long studentId;
    private Long teacherId;
    private Long categoryId;
    private Long courseId;

    @BeforeEach
    void setup() {
        // teacher
        User teacher = userRepository.save(User.builder()
                .name("Teacher")
                .email("t" + System.nanoTime() + "@mail.com")
                .role(Role.TEACHER)
                .build());
        teacherId = teacher.getId();

        // student
        User student = userRepository.save(User.builder()
                .name("Student")
                .email("s" + System.nanoTime() + "@mail.com")
                .role(Role.STUDENT)
                .build());
        studentId = student.getId();

        Category cat = categoryRepository.save(Category.builder()
                .name("Programming-" + System.nanoTime())
                .build());
        categoryId = cat.getId();

        Course course = courseRepository.save(Course.builder()
                .title("JPA")
                .description("Intro")
                .duration("2 weeks")
                .startDate(LocalDate.now())
                .teacher(teacher)
                .category(cat)
                .build());
        courseId = course.getId();
    }

    @Test
    void enroll_then_list_then_cancel() throws Exception {
        // enroll
        mockMvc.perform(post("/api/courses/{courseId}/enrollments", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":" + studentId + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.studentId").value(studentId))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        // list by course
        mockMvc.perform(get("/api/courses/{courseId}/enrollments", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(studentId));

        // cancel (status -> CANCELLED)
        mockMvc.perform(delete("/api/courses/{courseId}/enrollments/{studentId}", courseId, studentId))
                .andExpect(status().isNoContent());

        // list by student (status CANCELLED)
        mockMvc.perform(get("/api/students/{studentId}/enrollments", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(courseId))
                .andExpect(jsonPath("$[0].status").value("CANCELLED"));
    }

    @Test
    void enrolling_twice_returns400() throws Exception {
        mockMvc.perform(post("/api/courses/{courseId}/enrollments", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":" + studentId + "}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/courses/{courseId}/enrollments", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":" + studentId + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }
}

package com.example.eduplatform;

import com.example.eduplatform.entity.Category;
import com.example.eduplatform.entity.Course;
import com.example.eduplatform.entity.Role;
import com.example.eduplatform.entity.User;
import com.example.eduplatform.repository.CategoryRepository;
import com.example.eduplatform.repository.CourseRepository;
import com.example.eduplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CourseApiIT {

    @Autowired MockMvc mvc;
    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired CourseRepository courseRepository;

    @Test
    void listCourses_returns200() throws Exception {
        var teacher = userRepository.save(User.builder().name("T").email("t@t.com").role(Role.TEACHER).build());
        var category = categoryRepository.save(Category.builder().name("Programming").build());
        courseRepository.save(Course.builder().title("Java").description("x").teacher(teacher).category(category).build());

        mvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void patchCourse_updatesTitle() throws Exception {
        var teacher = userRepository.save(User.builder().name("T2").email("t2@t.com").role(Role.TEACHER).build());
        var category = categoryRepository.save(Category.builder().name("DB").build());
        var course = courseRepository.save(Course.builder().title("Old").description("x").teacher(teacher).category(category).build());

        mvc.perform(patch("/api/courses/" + course.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"New Title"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }
}

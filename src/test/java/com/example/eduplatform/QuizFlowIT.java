package com.example.eduplatform;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.repository.CategoryRepository;
import com.example.eduplatform.repository.UserRepository;
import com.example.eduplatform.service.CourseService;
import com.example.eduplatform.service.QuizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuizFlowIT {

    @Autowired CourseService courseService;
    @Autowired QuizService quizService;
    @Autowired CategoryRepository categoryRepository;
    @Autowired UserRepository userRepository;

    @Test
    void createQuiz_addQuestions_options_and_takeQuiz() {
        var cat = categoryRepository.save(Category.builder().name("Testing").build());
        var teacher = userRepository.save(User.builder().name("Tq").email("tq@x.com").role(Role.TEACHER).build());
        var student = userRepository.save(User.builder().name("Sq").email("sq@x.com").role(Role.STUDENT).build());

        var course = courseService.createCourse(new CreateCourseRequest("Course", "desc", cat.getId(), teacher.getId()));
        var module = courseService.addModule(course.id(), new CreateModuleRequest("M1", 1, null));

        Quiz quiz = quizService.createQuiz(new CreateQuizRequest(module.id(), "Quiz 1", null));

        Question q1 = quizService.createQuestion(new CreateQuestionRequest(quiz.getId(), "2+2?", QuestionType.SINGLE_CHOICE));
        AnswerOption o11 = quizService.createOption(new CreateAnswerOptionRequest(q1.getId(), "3", false));
        AnswerOption o12 = quizService.createOption(new CreateAnswerOptionRequest(q1.getId(), "4", true));

        Question q2 = quizService.createQuestion(new CreateQuestionRequest(quiz.getId(), "Capital of France?", QuestionType.SINGLE_CHOICE));
        AnswerOption o21 = quizService.createOption(new CreateAnswerOptionRequest(q2.getId(), "Paris", true));
        AnswerOption o22 = quizService.createOption(new CreateAnswerOptionRequest(q2.getId(), "Berlin", false));

        var result = quizService.takeQuiz(new TakeQuizRequest(
                student.getId(),
                quiz.getId(),
                Map.of(
                        q1.getId(), o12.getId(), // правильный
                        q2.getId(), o22.getId()  // неправильный
                )
        ));

        assertThat(result.score()).isEqualTo(1);

        assertThatThrownBy(() -> quizService.takeQuiz(new TakeQuizRequest(
                student.getId(),
                quiz.getId(),
                Map.of(q1.getId(), o12.getId())
        ))).isInstanceOf(RuntimeException.class);
    }
}

package com.example.eduplatform.controller;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.AnswerOption;
import com.example.eduplatform.entity.Question;
import com.example.eduplatform.entity.Quiz;
import com.example.eduplatform.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/quizzes")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createQuiz(@RequestBody @Valid CreateQuizRequest req) {
        return new IdResponse(quizService.createQuiz(req).getId());
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createQuestion(@RequestBody @Valid CreateQuestionRequest req) {
        return new IdResponse(quizService.createQuestion(req).getId());
    }

    @PostMapping("/options")
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createOption(@RequestBody @Valid CreateAnswerOptionRequest req) {
        return new IdResponse(quizService.createOption(req).getId());
    }

    @PostMapping("/quizzes/take")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizSubmissionResponse take(@RequestBody @Valid TakeQuizRequest req) {
        return quizService.takeQuiz(req);
    }
}

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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Quiz createQuiz(CreateQuizRequest req) {
        CourseModule module = courseModuleRepository.findById(req.moduleId())
                .orElseThrow(() -> new NotFoundException("Module not found: " + req.moduleId()));

        quizRepository.findByCourseModuleId(module.getId())
                .ifPresent(q -> { throw new BadRequestException("Quiz already exists for module"); });

        Quiz quiz = Quiz.builder()
                .title(req.title())
                .timeLimit(req.timeLimit())
                .courseModule(module)
                .build();

        return quizRepository.save(quiz);
    }

    @Transactional
    public Question createQuestion(CreateQuestionRequest req) {
        Quiz quiz = quizRepository.findById(req.quizId())
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + req.quizId()));

        Question q = Question.builder()
                .quiz(quiz)
                .text(req.text())
                .type(req.type())
                .build();

        return questionRepository.save(q);
    }

    @Transactional
    public AnswerOption createOption(CreateAnswerOptionRequest req) {
        Question question = questionRepository.findById(req.questionId())
                .orElseThrow(() -> new NotFoundException("Question not found: " + req.questionId()));

        AnswerOption opt = AnswerOption.builder()
                .question(question)
                .text(req.text())
                .isCorrect(req.isCorrect())
                .build();

        return answerOptionRepository.save(opt);
    }

    @Transactional
    public QuizSubmissionResponse takeQuiz(TakeQuizRequest req) {
        User student = userRepository.findById(req.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found: " + req.studentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new BadRequestException("User is not a STUDENT: " + req.studentId());
        }

        Quiz quiz = quizRepository.findById(req.quizId())
                .orElseThrow(() -> new NotFoundException("Quiz not found: " + req.quizId()));

        quizSubmissionRepository.findByStudentIdAndQuizId(req.studentId(), req.quizId())
                .ifPresent(s -> { throw new BadRequestException("Quiz already taken"); });

        // Подсчёт: 1 балл за каждый вопрос, где выбран правильный option
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());
        Map<Long, Long> answers = req.answers();

        int score = 0;
        for (Question q : questions) {
            Long selectedOptionId = answers.get(q.getId());
            if (selectedOptionId == null) continue;

            AnswerOption selected = answerOptionRepository.findById(selectedOptionId)
                    .orElseThrow(() -> new BadRequestException("Selected option not found: " + selectedOptionId));

            // защита от "подсунули option от другого вопроса"
            if (!selected.getQuestion().getId().equals(q.getId())) {
                throw new BadRequestException("Option does not belong to question: " + q.getId());
            }

            if (selected.isCorrect()) score++;
        }

        QuizSubmission sub = QuizSubmission.builder()
                .quiz(quiz)
                .student(student)
                .score(score)
                .takenAt(LocalDateTime.now())
                .build();

        QuizSubmission saved = quizSubmissionRepository.save(sub);

        return new QuizSubmissionResponse(
                saved.getId(),
                quiz.getId(),
                student.getId(),
                saved.getScore(),
                saved.getTakenAt()
        );
    }
}

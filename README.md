🎓 Edu Platform — система онлайн-обучения

Backend-приложение для управления онлайн-курсами, заданиями и тестированием студентов.
Реализовано на Spring Boot + JPA (Hibernate) с использованием PostgreSQL и Docker.

Проект демонстрирует работу с:

сложной объектной моделью,

связями между сущностями (1–1, 1–M, M–M),

REST API,

транзакциями и бизнес-логикой,

интеграционными тестами.

🧱 Архитектура проекта
Слои приложения
controller   – REST API
service      – бизнес-логика
repository   – доступ к данным (Spring Data JPA)
entity       – JPA-сущности
dto          – DTO (request / response)
exception    – централизованная обработка ошибок

📦 Основные сущности
Пользователи и роли

User

Role

Курсы и обучение

Course

Category

CourseModule

Lesson

Enrollment

EnrollmentStatus

Задания и тестирование

Assignment

Submission

Quiz

Question

AnswerOption

QuizSubmission

QuestionType

Все связи реализованы через JPA-аннотации (@OneToMany, @ManyToOne, @ManyToMany)
По умолчанию используется LAZY загрузка.

⚙️ Используемые технологии

Java 17

Spring Boot 3

Spring Data JPA (Hibernate)

PostgreSQL

Docker / Docker Compose

JUnit 5

Maven

🗄️ База данных

Используется PostgreSQL.
Схема создаётся автоматически при запуске приложения.

Подключение настраивается через application.yml и переменные окружения.

🌍 Переменные окружения
DB_HOST=localhost
DB_PORT=5432
DB_NAME=edu_platform
DB_USERNAME=postgres
DB_PASSWORD=postgres

SPRING_PROFILES_ACTIVE=dev

🚀 Запуск проекта
🔹 Локально (через Maven)
mvn clean spring-boot:run

🔹 Через Docker
docker-compose up --build


После запуска приложение доступно по адресу:

http://localhost:8080

🧪 Postman

Для тестирования API можно использовать Postman.

В проекте реализованы интеграционные тесты:

создание сущностей

проверка связей

проверка бизнес-логики (enroll, submit, validation)

проверка ошибок

Запуск тестов:

mvn test

🔗 REST API
Пользователи
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}

Курсы
GET    /api/courses
GET    /api/courses/{id}
POST   /api/courses
PUT    /api/courses/{id}
DELETE /api/courses/{id}

Запись на курс
POST   /api/enrollments/enroll?studentId={id}&courseId={id}
POST   /api/enrollments/unenroll?studentId={id}&courseId={id}
GET    /api/enrollments

Модули
GET    /api/modules
POST   /api/modules
PUT    /api/modules/{id}
DELETE /api/modules/{id}

Уроки
GET    /api/lessons
POST   /api/lessons
PUT    /api/lessons/{id}
DELETE /api/lessons/{id}

Задания
GET    /api/assignments
POST   /api/assignments
PUT    /api/assignments/{id}
DELETE /api/assignments/{id}

Решения заданий
GET    /api/submissions
GET    /api/submissions/student/{studentId}
GET    /api/submissions/assignment/{assignmentId}
POST   /api/submissions/submit

Тесты
GET    /api/quizzes
GET    /api/quizzes/{id}
POST   /api/quizzes
PUT    /api/quizzes/{id}
DELETE /api/quizzes/{id}

Прохождение тестов
POST /api/quiz-submissions/submit?quizId={id}&studentId={id}
GET  /api/quiz-submissions/student/{studentId}
GET  /api/quiz-submissions/course/{courseId}

🧩 Обработка ошибок

Используется глобальный @RestControllerAdvice:

BadRequestException

NotFoundException

MethodArgumentNotValidException

INTERNAL_ERROR

Все ошибки возвращаются в формате:

{
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/api/...",
  "timestamp": "2025-01-01T12:00:00Z",
  "errors": {
    "field": "message"
  }
}


Quizzes

(по аналогии с эндпоинтами выше)

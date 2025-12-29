# 📘 EduPlatform — учебная платформа (Spring Boot)

## Учебный проект, реализующий платформу для онлайн-обучения.
### Система поддерживает курсы, пользователей, задания, тесты и прохождение обучения.

### Проект разработан с использованием Spring Boot + JPA (Hibernate) + PostgreSQL, с поддержкой Docker и интеграционных тестов.

## 🧩 Архитектура проекта

Проект построен по классической многослойной архитектуре:
```
Controller → Service → Repository → Database
```

### Основные слои:

- controller — REST API (точки входа)
- service — бизнес-логика
- repository — доступ к данным (Spring Data JPA)
- entity — JPA-сущности
- dto — DTO для запросов и ответов
- exception — обработка ошибок
- config / resources — конфигурация приложения

### 🧠 Используемые технологии

- Java 17
- Spring Boot 3.x
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- Docker / Docker Compose
- JUnit (интеграционные тесты)

### 📦 Структура проекта
```
edu-platform/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/eduplatform/
│   │   │   ├── controller/        # REST контроллеры
│   │   │   ├── service/           # Бизнес-логика
│   │   │   ├── repository/        # JPA репозитории
│   │   │   ├── entity/            # JPA сущности
│   │   │   ├── dto/               # DTO объекты
│   │   │   ├── exception/         # Кастомные исключения
│   │   │   └── EduPlatformApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-test.yml
│   │
│   └── test/
│       └── java/com/example/eduplatform/
│           └── (интеграционные тесты)
│
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```
### 📚 Основные сущности (Entity)

- User
- Course
- Category
- Enrollment
- CourseModule
- Lesson
- Assignment
- Submission
- Quiz
- Question
- AnswerOption
- QuizSubmission
- Role

## Связи реализованы через @OneToMany, @ManyToOne, @ManyToMany с ленивой загрузкой (LAZY).

### ⚙️ Конфигурация
Переменные окружения (пример)
```DB_HOST=localhost
DB_PORT=5432
DB_NAME=edu_platform
DB_USERNAME=postgres
DB_PASSWORD=postgres

SPRING_PROFILES_ACTIVE=dev
```

### 🚀 Запуск проекта
🔹 Локально (без Docker)
```
mvn clean install
mvn spring-boot:run
```


Приложение будет доступно по адресу:
```
http://localhost:8080
```

🔹 Через Docker
```
docker-compose up --build
```



🧪 Тестирование
Запуск всех тестов:
```
mvn test
```
С использованием профиля test:
```
mvn test -P test
```

🔌 REST API (основные эндпоинты)
Пользователи
```
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
```

Курсы
```
GET    /api/courses
GET    /api/courses/{id}
POST   /api/courses
PUT    /api/courses/{id}
DELETE /api/courses/{id}
```

Запись на курс
```
POST   /api/enrollments/enroll?studentId=&courseId=
POST   /api/enrollments/unenroll?studentId=&courseId=
```

Модули и уроки
```
GET /api/modules
GET /api/lessons
```

Задания и отправки
```
GET  /api/assignments
POST /api/submissions/submit
```

Тесты
```
GET  /api/quizzes
POST /api/quizzes/{id}/take
```


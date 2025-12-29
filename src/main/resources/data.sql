INSERT INTO users (id, name, email, role) VALUES
(1, 'Teacher', 'teacher@mail.com', 'TEACHER'),
(2, 'Student', 'student@mail.com', 'STUDENT');

INSERT INTO category (id, name) VALUES (1, 'Programming');

INSERT INTO course (id, title, description, category_id, teacher_id)
VALUES (1, 'Java Basics', 'Intro course', 1, 1);

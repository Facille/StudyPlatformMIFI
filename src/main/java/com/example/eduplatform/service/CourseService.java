package com.example.eduplatform.service;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.entity.*;
import com.example.eduplatform.exception.NotFoundException;
import com.example.eduplatform.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseResponse createCourse(CreateCourseRequest req) {
        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + req.categoryId()));

        User teacher = userRepository.findById(req.teacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + req.teacherId()));

        Course course = Course.builder()
                .title(req.title())
                .description(req.description())
                .category(category)
                .teacher(teacher)
                .build();

        return toCourseResponse(courseRepository.save(course));
    }

    @Transactional
    public ModuleResponse addModule(Long courseId, CreateModuleRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        CourseModule module = CourseModule.builder()
                .title(req.title())
                .orderIndex(req.orderIndex())
                .description(req.description())
                .course(course)
                .build();

        return toModuleResponse(courseModuleRepository.save(module));
    }

    @Transactional
    public LessonResponse addLesson(Long moduleId, CreateLessonRequest req) {
        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Module not found: " + moduleId));

        Lesson lesson = Lesson.builder()
                .title(req.title())
                .content(req.content())
                .videoUrl(req.videoUrl())
                .courseModule(module)
                .build();

        return toLessonResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public CourseResponse getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));


        return toCourseResponse(course);
    }

    @Transactional
    public java.util.List<CourseResponse> listCourses() {
        return courseRepository.findAll().stream()
                .map(this::toCourseResponse)
                .toList();
    }

    @Transactional
    public CourseResponse updateCourse(Long courseId, UpdateCourseRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));

        if (req.title() != null) {
            course.setTitle(req.title());
        }
        if (req.description() != null) {
            course.setDescription(req.description());
        }
        if (req.categoryId() != null) {
            Category category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found: " + req.categoryId()));
            course.setCategory(category);
        }
        if (req.teacherId() != null) {
            User teacher = userRepository.findById(req.teacherId())
                    .orElseThrow(() -> new NotFoundException("Teacher not found: " + req.teacherId()));
            course.setTeacher(teacher);
        }

        return toCourseResponse(course);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found: " + courseId));


        if (!course.getCourseModules().isEmpty()) {
            throw new com.example.eduplatform.exception.BadRequestException(
                    "Cannot delete course with modules. Delete modules first."
            );
        }

        courseRepository.delete(course);
    }


    private CourseResponse toCourseResponse(Course course) {
        var modules = course.getCourseModules().stream()
                .map(this::toModuleResponse)
                .collect(Collectors.toList());

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory().getId(),
                course.getTeacher().getId(),
                modules
        );
    }

    private ModuleResponse toModuleResponse(CourseModule module) {
        var lessons = module.getLessons().stream()
                .map(this::toLessonResponse)
                .collect(Collectors.toList());

        return new ModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getOrderIndex(),
                module.getDescription(),
                lessons
        );
    }

    private LessonResponse toLessonResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getVideoUrl()
        );
    }
}

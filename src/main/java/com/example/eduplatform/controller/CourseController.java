package com.example.eduplatform.controller;

import com.example.eduplatform.dto.*;
import com.example.eduplatform.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@RequestBody @Valid CreateCourseRequest req) {
        return courseService.createCourse(req);
    }

    @GetMapping("/courses/{id}")
    public CourseResponse getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @PostMapping("/courses/{courseId}/modules")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse addModule(@PathVariable Long courseId, @RequestBody @Valid CreateModuleRequest req) {
        return courseService.addModule(courseId, req);
    }

    @PostMapping("/modules/{moduleId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse addLesson(@PathVariable Long moduleId, @RequestBody @Valid CreateLessonRequest req) {
        return courseService.addLesson(moduleId, req);
    }
    @GetMapping("/courses")
    public java.util.List<CourseResponse> listCourses() {
        return courseService.listCourses();
    }

    @GetMapping("/courses/{id}/structure")
    public CourseResponse getCourseStructure(@PathVariable Long id) {
        return courseService.getCourse(id); // можно переиспользовать
    }

    @PatchMapping("/courses/{id}")
    public CourseResponse updateCourse(@PathVariable Long id, @RequestBody @Valid UpdateCourseRequest req) {
        return courseService.updateCourse(id, req);
    }

    @DeleteMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

}

package com.learnsphere.lesson.controller;



import com.learnsphere.lesson.entity.Lesson;
import com.learnsphere.lesson.service.LessonService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }


    // ================= ADD LESSON =================

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson addLesson(
            @PathVariable Long courseId,
            @RequestBody Lesson lesson) {

        return lessonService.addLesson(courseId, lesson);
    }


    // ================= GET ALL LESSONS OF COURSE =================

    @GetMapping
    public List<Lesson> getLessons(
            @PathVariable Long courseId) {

        return lessonService.getLessonsByCourse(courseId);
    }


    // ================= GET ONE LESSON =================

    @GetMapping("/{lessonId}")
    public Lesson getLesson(
            @PathVariable Long lessonId) {

        return lessonService.getLessonById(lessonId);
    }


    // ================= DELETE LESSON =================

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(
            @PathVariable Long lessonId) {

        lessonService.deleteLesson(lessonId);
    }
    // ================= UPDATE LESSON =================

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PutMapping("/{lessonId}")
    public Lesson updateLesson(
            @PathVariable Long lessonId,
            @RequestBody Lesson lesson) {

        return lessonService.updateLesson(
                lessonId,
                lesson
        );
    }
}

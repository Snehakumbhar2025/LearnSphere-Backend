package com.learnsphere.lessonprogress.controller;

import com.learnsphere.lessonprogress.dto.CourseProgressResponse;
import com.learnsphere.lessonprogress.entity.LessonProgress;
import com.learnsphere.lessonprogress.service.LessonProgressService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/progress")
public class LessonProgressController {

    private final LessonProgressService lessonProgressService;


    // =====================================================
    // ================= CONSTRUCTOR ========================
    // =====================================================

    public LessonProgressController(
            LessonProgressService lessonProgressService
    ) {

        this.lessonProgressService =
                lessonProgressService;
    }


    // =====================================================
    // ================= MARK LESSON COMPLETE ===============
    // =====================================================

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/lessons/{lessonId}/complete")
    public LessonProgress markLessonComplete(
            @PathVariable Long lessonId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();


        return lessonProgressService
                .markLessonComplete(
                        email,
                        lessonId
                );
    }


    // =====================================================
    // ================= GET LESSON PROGRESS ================
    // =====================================================

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}")
    public List<LessonProgress> getCourseProgress(
            @PathVariable Long courseId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();


        return lessonProgressService
                .getCourseProgress(
                        email,
                        courseId
                );
    }


    // =====================================================
    // ============== GET COURSE PROGRESS DETAILS ===========
    // =====================================================

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}/details")
    public CourseProgressResponse getCourseProgressDetails(
            @PathVariable Long courseId,
            Authentication authentication
    ) {

        String email =
                authentication.getName();


        return lessonProgressService
                .getCourseProgressDetails(
                        email,
                        courseId
                );
    }
}
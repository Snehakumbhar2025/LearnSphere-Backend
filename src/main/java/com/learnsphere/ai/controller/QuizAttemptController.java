package com.learnsphere.ai.controller;

import com.learnsphere.ai.dto.QuizAnalyticsResponse;
import com.learnsphere.ai.entity.QuizAttempt;
import com.learnsphere.ai.service.QuizAttemptService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.learnsphere.ai.dto.LessonQuizAnalyticsResponse;
import java.util.List;
import java.util.Map;
import com.learnsphere.ai.dto.AiLearningInsightResponse;
@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    // ================= CONSTRUCTOR =================

    public QuizAttemptController(
            QuizAttemptService quizAttemptService
    ) {
        this.quizAttemptService = quizAttemptService;
    }


    // =====================================================
    // ================= SAVE ATTEMPT =======================
    // =====================================================

    @PostMapping
    public ResponseEntity<QuizAttempt> saveAttempt(
            @RequestBody Map<String, Object> request
    ) {

        Long lessonId =
                Long.valueOf(
                        request.get("lessonId").toString()
                );

        Integer score =
                Integer.valueOf(
                        request.get("score").toString()
                );

        Integer totalQuestions =
                Integer.valueOf(
                        request.get("totalQuestions").toString()
                );

        QuizAttempt savedAttempt =
                quizAttemptService.saveAttempt(
                        lessonId,
                        score,
                        totalQuestions
                );

        return ResponseEntity.ok(savedAttempt);
    }


    // =====================================================
    // ================= MY QUIZ HISTORY ====================
    // =====================================================

    @GetMapping("/my")
    public ResponseEntity<List<QuizAttempt>> getMyAttempts() {

        List<QuizAttempt> attempts =
                quizAttemptService.getMyAttempts();

        return ResponseEntity.ok(attempts);
    }


    // =====================================================
    // ============== MY ATTEMPTS FOR LESSON ==============
    // =====================================================

    @GetMapping("/my/lesson/{lessonId}")
    public ResponseEntity<List<QuizAttempt>> getMyLessonAttempts(
            @PathVariable Long lessonId
    ) {

        List<QuizAttempt> attempts =
                quizAttemptService
                        .getMyLessonAttempts(lessonId);

        return ResponseEntity.ok(attempts);
    }


    // =====================================================
    // ================= MY QUIZ ANALYTICS ==================
    // =====================================================

    @GetMapping("/my/analytics")
    public ResponseEntity<QuizAnalyticsResponse> getMyAnalytics() {

        QuizAnalyticsResponse analytics =
                quizAttemptService.getMyAnalytics();

        return ResponseEntity.ok(analytics);
    }
// =====================================================
// ============== LESSON-WISE ANALYTICS ================
// =====================================================

    @GetMapping("/my/analytics/lessons")
    public ResponseEntity<List<LessonQuizAnalyticsResponse>>
    getMyLessonAnalytics() {

        List<LessonQuizAnalyticsResponse> analytics =
                quizAttemptService.getMyLessonAnalytics();

        System.out.println(
                "LESSON ANALYTICS RESULT: " + analytics.size()
        );

        return ResponseEntity.ok(analytics);
    }

    // =====================================================
// ============== LEARNING INSIGHT ======================
// =====================================================

    @GetMapping("/my/learning-insight")
    public ResponseEntity<AiLearningInsightResponse>
    getMyLearningInsight() {

        AiLearningInsightResponse response =
                quizAttemptService.getMyLearningInsight();

        return ResponseEntity.ok(response);
    }
}
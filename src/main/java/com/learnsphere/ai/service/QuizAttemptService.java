package com.learnsphere.ai.service;

import com.learnsphere.ai.dto.AiLearningInsightResponse;
import com.learnsphere.ai.dto.LessonQuizAnalyticsResponse;
import com.learnsphere.ai.dto.QuizAnalyticsResponse;
import com.learnsphere.ai.entity.QuizAttempt;
import com.learnsphere.ai.repository.QuizAttemptRepository;

import com.learnsphere.entity.User;

import com.learnsphere.lesson.entity.Lesson;
import com.learnsphere.lesson.repository.LessonRepository;

import com.learnsphere.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final AiService aiService;


    // =====================================================
    // ================= CONSTRUCTOR ========================
    // =====================================================

    public QuizAttemptService(
            QuizAttemptRepository quizAttemptRepository,
            LessonRepository lessonRepository,
            UserRepository userRepository,
            AiService aiService
    ) {

        this.quizAttemptRepository =
                quizAttemptRepository;

        this.lessonRepository =
                lessonRepository;

        this.userRepository =
                userRepository;

        this.aiService =
                aiService;
    }


    // =====================================================
    // ================= SAVE QUIZ ATTEMPT ==================
    // =====================================================

    public QuizAttempt saveAttempt(
            Long lessonId,
            Integer score,
            Integer totalQuestions
    ) {

        User user =
                getCurrentUser();


        // ================= FIND LESSON =================

        Lesson lesson =
                lessonRepository
                        .findById(lessonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lesson not found"
                                )
                        );


        // ================= VALIDATE RESULT =================

        if (
                totalQuestions == null ||
                        totalQuestions <= 0
        ) {

            throw new RuntimeException(
                    "Total questions must be greater than 0"
            );
        }


        if (
                score == null ||
                        score < 0 ||
                        score > totalQuestions
        ) {

            throw new RuntimeException(
                    "Invalid quiz score"
            );
        }


        // ================= CALCULATE PERCENTAGE =================

        double percentage =
                ((double) score / totalQuestions)
                        * 100;


        percentage =
                Math.round(
                        percentage * 100.0
                ) / 100.0;


        // ================= CREATE ATTEMPT =================

        QuizAttempt attempt =
                new QuizAttempt(
                        user,
                        lesson,
                        score,
                        totalQuestions,
                        percentage
                );


        // ================= SAVE =================

        return quizAttemptRepository
                .save(attempt);
    }


    // =====================================================
    // ================= USER QUIZ HISTORY ==================
    // =====================================================

    public List<QuizAttempt> getMyAttempts() {

        User user =
                getCurrentUser();


        return quizAttemptRepository
                .findByUserIdOrderByAttemptedAtDesc(
                        user.getId()
                );
    }


    // =====================================================
    // =============== USER LESSON HISTORY ==================
    // =====================================================

    public List<QuizAttempt> getMyLessonAttempts(
            Long lessonId
    ) {

        User user =
                getCurrentUser();


        return quizAttemptRepository
                .findByUserIdAndLessonIdOrderByAttemptedAtDesc(
                        user.getId(),
                        lessonId
                );
    }


    // =====================================================
    // ================= GET CURRENT USER ===================
    // =====================================================

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (
                authentication == null ||
                        !authentication.isAuthenticated()
        ) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }


        String email =
                authentication.getName();


        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Logged-in user not found"
                        )
                );
    }


    // =====================================================
    // ================= QUIZ ANALYTICS =====================
    // =====================================================

    public QuizAnalyticsResponse getMyAnalytics() {

        User user =
                getCurrentUser();


        List<QuizAttempt> attempts =
                quizAttemptRepository
                        .findByUserIdOrderByAttemptedAtDesc(
                                user.getId()
                        );


        // ================= NO ATTEMPTS =================

        if (attempts.isEmpty()) {

            return new QuizAnalyticsResponse(
                    0,
                    0.0,
                    0.0
            );
        }


        long totalAttempts =
                attempts.size();


        double totalPercentage =
                0.0;


        double bestPercentage =
                0.0;


        for (QuizAttempt attempt : attempts) {

            double percentage =
                    attempt.getPercentage();


            totalPercentage +=
                    percentage;


            if (percentage > bestPercentage) {

                bestPercentage =
                        percentage;
            }
        }


        double averagePercentage =
                totalPercentage /
                        totalAttempts;


        averagePercentage =
                Math.round(
                        averagePercentage * 100.0
                ) / 100.0;


        bestPercentage =
                Math.round(
                        bestPercentage * 100.0
                ) / 100.0;


        return new QuizAnalyticsResponse(
                totalAttempts,
                averagePercentage,
                bestPercentage
        );
    }


    // =====================================================
    // ============== LESSON-WISE ANALYTICS ================
    // =====================================================

    public List<LessonQuizAnalyticsResponse>
    getMyLessonAnalytics() {

        User user =
                getCurrentUser();


        List<QuizAttempt> attempts =
                quizAttemptRepository
                        .findByUserIdOrderByAttemptedAtDesc(
                                user.getId()
                        );


        // ================= GROUP BY LESSON =================

        Map<Long, List<QuizAttempt>>
                attemptsByLesson =
                new LinkedHashMap<>();


        for (QuizAttempt attempt : attempts) {

            Long lessonId =
                    attempt
                            .getLesson()
                            .getId();


            attemptsByLesson
                    .computeIfAbsent(
                            lessonId,
                            key -> new ArrayList<>()
                    )
                    .add(attempt);
        }


        List<LessonQuizAnalyticsResponse>
                analytics =
                new ArrayList<>();


        // ================= CALCULATE EACH LESSON =================

        for (
                Map.Entry<Long, List<QuizAttempt>> entry :
                attemptsByLesson.entrySet()
        ) {

            List<QuizAttempt> lessonAttempts =
                    entry.getValue();


            if (lessonAttempts.isEmpty()) {

                continue;
            }


            QuizAttempt firstAttempt =
                    lessonAttempts.get(0);


            Long lessonId =
                    firstAttempt
                            .getLesson()
                            .getId();


            String lessonTitle =
                    firstAttempt
                            .getLesson()
                            .getTitle();


            long totalAttempts =
                    lessonAttempts.size();


            double totalPercentage =
                    0.0;


            double bestPercentage =
                    0.0;


            for (QuizAttempt attempt : lessonAttempts) {

                double percentage =
                        attempt.getPercentage();


                totalPercentage +=
                        percentage;


                if (percentage > bestPercentage) {

                    bestPercentage =
                            percentage;
                }
            }


            double averagePercentage =
                    totalPercentage /
                            totalAttempts;


            averagePercentage =
                    Math.round(
                            averagePercentage * 100.0
                    ) / 100.0;


            bestPercentage =
                    Math.round(
                            bestPercentage * 100.0
                    ) / 100.0;


            analytics.add(

                    new LessonQuizAnalyticsResponse(
                            lessonId,
                            lessonTitle,
                            totalAttempts,
                            averagePercentage,
                            bestPercentage
                    )

            );
        }


        return analytics;
    }


    // =====================================================
    // ============== AI LEARNING INSIGHT ===================
    // =====================================================

    public AiLearningInsightResponse
    getMyLearningInsight() {

        User user =
                getCurrentUser();


        // ================= GET ATTEMPTS =================

        List<QuizAttempt> attempts =
                quizAttemptRepository
                        .findByUserIdOrderByAttemptedAtDesc(
                                user.getId()
                        );


        // ================= NO ATTEMPTS =================

        if (attempts.isEmpty()) {

            return new AiLearningInsightResponse(
                    null,
                    null,
                    null,
                    null,
                    0.0,
                    0.0,
                    "Complete some quizzes so LearnSphere AI can analyze your performance."
            );
        }


        // =====================================================
        // ================= GROUP BY LESSON ====================
        // =====================================================

        Map<Long, List<QuizAttempt>>
                attemptsByLesson =
                attempts
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        attempt ->
                                                attempt
                                                        .getLesson()
                                                        .getId()
                                )
                        );


        String strongestLesson =
                null;


        String weakestLesson =
                null;


        // Exact lesson to recommend
        Long weakestLessonId =
                null;


        // Course containing the weakest lesson
        Long weakestCourseId =
                null;


        double strongestScore =
                -1.0;


        double weakestScore =
                101.0;


        // =====================================================
        // ============== CALCULATE LESSON AVERAGES ============
        // =====================================================

        for (
                List<QuizAttempt> lessonAttempts :
                attemptsByLesson.values()
        ) {

            if (lessonAttempts.isEmpty()) {

                continue;
            }


            QuizAttempt firstAttempt =
                    lessonAttempts.get(0);


            Lesson lesson =
                    firstAttempt.getLesson();


            String lessonTitle =
                    lesson.getTitle();


            Long lessonId =
                    lesson.getId();


            double average =
                    lessonAttempts
                            .stream()
                            .mapToDouble(
                                    QuizAttempt::getPercentage
                            )
                            .average()
                            .orElse(0.0);


            // ================= STRONGEST =================

            if (average > strongestScore) {

                strongestScore =
                        average;


                strongestLesson =
                        lessonTitle;
            }


            // ================= WEAKEST =================

            if (average < weakestScore) {

                weakestScore =
                        average;


                weakestLesson =
                        lessonTitle;


                weakestLessonId =
                        lessonId;


                // Store the course containing this lesson
                if (lesson.getCourse() != null) {

                    weakestCourseId =
                            lesson
                                    .getCourse()
                                    .getId();

                } else {

                    weakestCourseId =
                            null;
                }
            }
        }


        // =====================================================
        // ================= ROUND SCORES =======================
        // =====================================================

        strongestScore =
                Math.round(
                        strongestScore * 100.0
                ) / 100.0;


        weakestScore =
                Math.round(
                        weakestScore * 100.0
                ) / 100.0;


        // =====================================================
        // ================ OVERALL AVERAGE =====================
        // =====================================================

        double overallAverage =
                attempts
                        .stream()
                        .mapToDouble(
                                QuizAttempt::getPercentage
                        )
                        .average()
                        .orElse(0.0);


        overallAverage =
                Math.round(
                        overallAverage * 100.0
                ) / 100.0;


        // =====================================================
        // ============== FALLBACK RECOMMENDATION ==============
        // =====================================================

        String fallbackRecommendation;


        if (
                strongestLesson != null &&
                        strongestLesson.equals(
                                weakestLesson
                        )
        ) {

            fallbackRecommendation =
                    "You currently have quiz data for only one lesson. "
                            + "Your average score in "
                            + strongestLesson
                            + " is "
                            + strongestScore
                            + "%. Attempt quizzes from more lessons "
                            + "to receive a more detailed personalized "
                            + "learning analysis.";

        } else {

            fallbackRecommendation =
                    "You are performing best in "
                            + strongestLesson
                            + " with an average score of "
                            + strongestScore
                            + "%. Your current focus area is "
                            + weakestLesson
                            + " with an average score of "
                            + weakestScore
                            + "%. Review this lesson carefully and "
                            + "attempt the quiz again to improve your score.";
        }


        // =====================================================
        // ================= ASK GEMINI AI ======================
        // =====================================================

        String aiRecommendation =
                aiService
                        .generateLearningRecommendation(
                                strongestLesson,
                                strongestScore,
                                weakestLesson,
                                weakestScore,
                                overallAverage,
                                fallbackRecommendation
                        );


        // =====================================================
        // ================= RETURN RESPONSE ====================
        // =====================================================

        return new AiLearningInsightResponse(
                strongestLesson,
                weakestLesson,
                weakestLessonId,
                weakestCourseId,
                strongestScore,
                weakestScore,
                aiRecommendation
        );
    }
}
package com.learnsphere.lessonprogress.service;

import com.learnsphere.course.entity.Course;
import com.learnsphere.enrollment.entity.Enrollment;
import com.learnsphere.enrollment.repository.EnrollmentRepository;
import com.learnsphere.entity.User;
import com.learnsphere.lesson.entity.Lesson;
import com.learnsphere.lesson.repository.LessonRepository;
import com.learnsphere.lessonprogress.dto.CourseProgressResponse;
import com.learnsphere.lessonprogress.entity.LessonProgress;
import com.learnsphere.lessonprogress.repository.LessonProgressRepository;
import com.learnsphere.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;


    // =====================================================
    // ================= CONSTRUCTOR ========================
    // =====================================================

    public LessonProgressService(
            LessonProgressRepository lessonProgressRepository,
            LessonRepository lessonRepository,
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository
    ) {

        this.lessonProgressRepository =
                lessonProgressRepository;

        this.lessonRepository =
                lessonRepository;

        this.enrollmentRepository =
                enrollmentRepository;

        this.userRepository =
                userRepository;
    }


    // =====================================================
    // ================= MARK LESSON COMPLETE ===============
    // =====================================================

    public LessonProgress markLessonComplete(
            String email,
            Long lessonId
    ) {

        // ================= FIND USER =================

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        // ================= FIND LESSON =================

        Lesson lesson =
                lessonRepository
                        .findById(lessonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lesson not found"
                                )
                        );


        // ================= GET COURSE =================

        Course course =
                lesson.getCourse();


        if (course == null) {

            throw new RuntimeException(
                    "Course not found for this lesson"
            );
        }


        // ================= CHECK ENROLLMENT =================

        Enrollment enrollment =
                enrollmentRepository
                        .findByUserIdAndCourseId(
                                user.getId(),
                                course.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "You are not enrolled in this course"
                                )
                        );


        // ================= CHECK EXISTING PROGRESS =================

        LessonProgress progress =
                lessonProgressRepository
                        .findByUserIdAndLessonId(
                                user.getId(),
                                lessonId
                        )
                        .orElse(null);


        // ================= CREATE PROGRESS =================

        if (progress == null) {

            progress =
                    LessonProgress
                            .builder()
                            .user(user)
                            .lesson(lesson)
                            .completed(true)
                            .completedAt(
                                    LocalDateTime.now()
                            )
                            .build();

        } else {

            // Lesson already has a progress record

            progress.setCompleted(true);


            if (
                    progress.getCompletedAt() == null
            ) {

                progress.setCompletedAt(
                        LocalDateTime.now()
                );
            }
        }


        // ================= SAVE LESSON PROGRESS =================

        LessonProgress savedProgress =
                lessonProgressRepository
                        .save(progress);


        // =====================================================
        // ============== CALCULATE COURSE PROGRESS ============
        // =====================================================

        List<Lesson> lessons =
                lessonRepository
                        .findByCourseIdOrderByLessonOrderAsc(
                                course.getId()
                        );


        int totalLessons =
                lessons.size();


        long completedLessons =
                lessonProgressRepository
                        .countByUserIdAndLessonCourseIdAndCompletedTrue(
                                user.getId(),
                                course.getId()
                        );


        int percentage =
                calculatePercentage(
                        completedLessons,
                        totalLessons
                );


        // =====================================================
        // ================= UPDATE ENROLLMENT ==================
        // =====================================================

        enrollment.setProgress(
                percentage
        );


        enrollment.setCompleted(
                totalLessons > 0 &&
                        completedLessons == totalLessons
        );


        enrollmentRepository
                .save(enrollment);


        return savedProgress;
    }


    // =====================================================
    // ================= GET LESSON PROGRESS ===============
    // =====================================================

    public List<LessonProgress> getCourseProgress(
            String email,
            Long courseId
    ) {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        // ================= CHECK ENROLLMENT =================

        enrollmentRepository
                .findByUserIdAndCourseId(
                        user.getId(),
                        courseId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "You are not enrolled in this course"
                        )
                );


        return lessonProgressRepository
                .findByUserIdAndLessonCourseId(
                        user.getId(),
                        courseId
                );
    }


    // =====================================================
    // ============== GET DETAILED COURSE PROGRESS ==========
    // =====================================================

    public CourseProgressResponse getCourseProgressDetails(
            String email,
            Long courseId
    ) {

        // ================= FIND USER =================

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        // ================= CHECK ENROLLMENT =================

        Enrollment enrollment =
                enrollmentRepository
                        .findByUserIdAndCourseId(
                                user.getId(),
                                courseId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "You are not enrolled in this course"
                                )
                        );


        // =====================================================
        // ================= GET COURSE LESSONS ================
        // =====================================================

        List<Lesson> lessons =
                lessonRepository
                        .findByCourseIdOrderByLessonOrderAsc(
                                courseId
                        );


        int totalLessons =
                lessons.size();


        // =====================================================
        // ================= GET USER PROGRESS ==================
        // =====================================================

        List<LessonProgress> progressRecords =
                lessonProgressRepository
                        .findByUserIdAndLessonCourseId(
                                user.getId(),
                                courseId
                        );


        // =====================================================
        // ============== COMPLETED LESSON IDS ==================
        // =====================================================

        Set<Long> completedLessonIds =
                new HashSet<>();


        for (
                LessonProgress progress :
                progressRecords
        ) {

            if (
                    progress.isCompleted() &&
                            progress.getLesson() != null
            ) {

                completedLessonIds.add(
                        progress
                                .getLesson()
                                .getId()
                );
            }
        }


        long completedLessons =
                completedLessonIds.size();


        // =====================================================
        // ================= CALCULATE PERCENTAGE ===============
        // =====================================================

        int progressPercentage =
                calculatePercentage(
                        completedLessons,
                        totalLessons
                );


        // =====================================================
        // ================= DETERMINE STATUS ====================
        // =====================================================

        String status;


        if (
                totalLessons > 0 &&
                        completedLessons >= totalLessons
        ) {

            status =
                    "COMPLETED";

        } else if (
                completedLessons > 0
        ) {

            status =
                    "IN_PROGRESS";

        } else {

            status =
                    "NOT_STARTED";
        }


        // =====================================================
        // ================= FIND NEXT LESSON ====================
        // =====================================================

        Long nextLessonId =
                null;


        // Lessons are already ordered by lessonOrder ASC

        for (Lesson lesson : lessons) {

            if (
                    !completedLessonIds.contains(
                            lesson.getId()
                    )
            ) {

                nextLessonId =
                        lesson.getId();

                break;
            }
        }


        // =====================================================
        // ============== KEEP ENROLLMENT SYNCHRONIZED =========
        // =====================================================

        enrollment.setProgress(
                progressPercentage
        );


        enrollment.setCompleted(
                "COMPLETED".equals(status)
        );


        enrollmentRepository
                .save(enrollment);


        // =====================================================
        // ================= RETURN RESPONSE ====================
        // =====================================================

        return new CourseProgressResponse(
                courseId,
                totalLessons,
                completedLessons,
                progressPercentage,
                status,
                nextLessonId
        );
    }


    // =====================================================
    // ================= CALCULATE PERCENTAGE ==============
    // =====================================================

    private int calculatePercentage(
            long completedLessons,
            int totalLessons
    ) {

        if (totalLessons <= 0) {

            return 0;
        }


        double percentage =
                ((double) completedLessons /
                        totalLessons)
                        * 100;


        return (int) Math.round(
                percentage
        );
    }
}
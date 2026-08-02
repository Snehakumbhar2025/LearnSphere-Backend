package com.learnsphere.lessonprogress.repository;



import com.learnsphere.lessonprogress.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository
        extends JpaRepository<LessonProgress, Long> {

    // Find progress for one particular student + lesson
    Optional<LessonProgress> findByUserIdAndLessonId(
            Long userId,
            Long lessonId
    );


    // Check whether progress record already exists
    boolean existsByUserIdAndLessonId(
            Long userId,
            Long lessonId
    );


    // Get all lesson progress of a student for a particular course
    List<LessonProgress> findByUserIdAndLessonCourseId(
            Long userId,
            Long courseId
    );


    // Count completed lessons of student in a particular course
    long countByUserIdAndLessonCourseIdAndCompletedTrue(
            Long userId,
            Long courseId
    );
}

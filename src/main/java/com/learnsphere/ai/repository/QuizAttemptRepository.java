package com.learnsphere.ai.repository;

import com.learnsphere.ai.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository
        extends JpaRepository<QuizAttempt, Long> {

    // ================= ALL ATTEMPTS FOR A LESSON =================

    List<QuizAttempt> findByLessonIdOrderByAttemptedAtDesc(
            Long lessonId
    );


    // ================= ALL ATTEMPTS BY A USER =================

    List<QuizAttempt> findByUserIdOrderByAttemptedAtDesc(
            Long userId
    );


    // ================= USER ATTEMPTS FOR ONE LESSON =================

    List<QuizAttempt> findByUserIdAndLessonIdOrderByAttemptedAtDesc(
            Long userId,
            Long lessonId
    );
}
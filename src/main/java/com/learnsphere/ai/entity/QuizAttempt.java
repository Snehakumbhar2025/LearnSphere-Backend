package com.learnsphere.ai.entity;

import com.learnsphere.entity.User;
import com.learnsphere.lesson.entity.Lesson;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ================= USER / STUDENT =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // ================= LESSON =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;


    // ================= QUIZ RESULT =================

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Double percentage;


    // ================= ATTEMPT TIME =================

    @Column(nullable = false)
    private LocalDateTime attemptedAt;


    // ================= DEFAULT CONSTRUCTOR =================

    public QuizAttempt() {
    }


    // ================= CONSTRUCTOR =================

    public QuizAttempt(
            User user,
            Lesson lesson,
            Integer score,
            Integer totalQuestions,
            Double percentage
    ) {

        this.user = user;
        this.lesson = lesson;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.attemptedAt = LocalDateTime.now();
    }


    // ================= AUTOMATIC TIMESTAMP =================

    @PrePersist
    public void prePersist() {

        if (attemptedAt == null) {
            attemptedAt = LocalDateTime.now();
        }
    }


    // ================= GETTERS / SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }


    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }


    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }


    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }
}
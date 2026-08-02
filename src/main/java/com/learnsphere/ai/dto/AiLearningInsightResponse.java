package com.learnsphere.ai.dto;

public class AiLearningInsightResponse {

    private String strongestLesson;
    private String weakestLesson;

    private Long weakestLessonId;

    // Course containing the weakest lesson
    private Long weakestCourseId;

    private Double strongestScore;
    private Double weakestScore;

    private String insight;


    // ================= EMPTY CONSTRUCTOR =================

    public AiLearningInsightResponse() {
    }


    // ================= CONSTRUCTOR =================

    public AiLearningInsightResponse(
            String strongestLesson,
            String weakestLesson,
            Long weakestLessonId,
            Long weakestCourseId,
            Double strongestScore,
            Double weakestScore,
            String insight
    ) {

        this.strongestLesson = strongestLesson;
        this.weakestLesson = weakestLesson;
        this.weakestLessonId = weakestLessonId;
        this.weakestCourseId = weakestCourseId;
        this.strongestScore = strongestScore;
        this.weakestScore = weakestScore;
        this.insight = insight;
    }


    // ================= GETTERS / SETTERS =================

    public String getStrongestLesson() {
        return strongestLesson;
    }

    public void setStrongestLesson(
            String strongestLesson
    ) {
        this.strongestLesson = strongestLesson;
    }


    public String getWeakestLesson() {
        return weakestLesson;
    }

    public void setWeakestLesson(
            String weakestLesson
    ) {
        this.weakestLesson = weakestLesson;
    }


    public Long getWeakestLessonId() {
        return weakestLessonId;
    }

    public void setWeakestLessonId(
            Long weakestLessonId
    ) {
        this.weakestLessonId = weakestLessonId;
    }


    public Long getWeakestCourseId() {
        return weakestCourseId;
    }

    public void setWeakestCourseId(
            Long weakestCourseId
    ) {
        this.weakestCourseId = weakestCourseId;
    }


    public Double getStrongestScore() {
        return strongestScore;
    }

    public void setStrongestScore(
            Double strongestScore
    ) {
        this.strongestScore = strongestScore;
    }


    public Double getWeakestScore() {
        return weakestScore;
    }

    public void setWeakestScore(
            Double weakestScore
    ) {
        this.weakestScore = weakestScore;
    }


    public String getInsight() {
        return insight;
    }

    public void setInsight(
            String insight
    ) {
        this.insight = insight;
    }
}
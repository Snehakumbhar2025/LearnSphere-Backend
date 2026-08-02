package com.learnsphere.ai.dto;

public class LessonQuizAnalyticsResponse {

    private Long lessonId;
    private String lessonTitle;

    private long totalAttempts;

    private double averagePercentage;
    private double bestPercentage;

    public LessonQuizAnalyticsResponse() {
    }

    public LessonQuizAnalyticsResponse(
            Long lessonId,
            String lessonTitle,
            long totalAttempts,
            double averagePercentage,
            double bestPercentage
    ) {
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.totalAttempts = totalAttempts;
        this.averagePercentage = averagePercentage;
        this.bestPercentage = bestPercentage;
    }


    // ================= GETTERS / SETTERS =================

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }


    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }


    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }


    public double getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }


    public double getBestPercentage() {
        return bestPercentage;
    }

    public void setBestPercentage(double bestPercentage) {
        this.bestPercentage = bestPercentage;
    }
}
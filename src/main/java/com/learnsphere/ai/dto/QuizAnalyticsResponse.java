package com.learnsphere.ai.dto;

public class QuizAnalyticsResponse {

    private long totalAttempts;
    private double averagePercentage;
    private double bestPercentage;

    public QuizAnalyticsResponse() {
    }

    public QuizAnalyticsResponse(
            long totalAttempts,
            double averagePercentage,
            double bestPercentage
    ) {
        this.totalAttempts = totalAttempts;
        this.averagePercentage = averagePercentage;
        this.bestPercentage = bestPercentage;
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

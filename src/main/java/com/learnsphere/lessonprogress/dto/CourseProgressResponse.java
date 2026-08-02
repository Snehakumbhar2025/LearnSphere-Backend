package com.learnsphere.lessonprogress.dto;

public class CourseProgressResponse {

    private Long courseId;

    private int totalLessons;

    private long completedLessons;

    private int progressPercentage;

    private String status;

    private Long nextLessonId;


    // ================= EMPTY CONSTRUCTOR =================

    public CourseProgressResponse() {
    }


    // ================= CONSTRUCTOR =================

    public CourseProgressResponse(
            Long courseId,
            int totalLessons,
            long completedLessons,
            int progressPercentage,
            String status,
            Long nextLessonId
    ) {

        this.courseId = courseId;

        this.totalLessons = totalLessons;

        this.completedLessons = completedLessons;

        this.progressPercentage = progressPercentage;

        this.status = status;

        this.nextLessonId = nextLessonId;
    }


    // ================= GETTERS / SETTERS =================

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }


    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }


    public long getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(long completedLessons) {
        this.completedLessons = completedLessons;
    }


    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(
            int progressPercentage
    ) {
        this.progressPercentage =
                progressPercentage;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Long getNextLessonId() {
        return nextLessonId;
    }

    public void setNextLessonId(
            Long nextLessonId
    ) {
        this.nextLessonId =
                nextLessonId;
    }
}
package com.learnsphere.ai.dto;



public class AiQuizRequest {

    private Long lessonId;

    private int numberOfQuestions = 5;


    public AiQuizRequest() {
    }


    public Long getLessonId() {
        return lessonId;
    }


    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }


    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }


    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
package com.learnsphere.ai.dto;



import java.util.List;

public class AiQuizResponse {

    private Long lessonId;

    private String lessonTitle;

    private List<AiQuizQuestion> questions;


    public AiQuizResponse() {
    }


    public AiQuizResponse(
            Long lessonId,
            String lessonTitle,
            List<AiQuizQuestion> questions
    ) {
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.questions = questions;
    }


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


    public List<AiQuizQuestion> getQuestions() {
        return questions;
    }


    public void setQuestions(List<AiQuizQuestion> questions) {
        this.questions = questions;
    }
}

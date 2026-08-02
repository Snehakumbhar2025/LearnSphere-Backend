package com.learnsphere.ai.dto;



import java.util.List;

public class AiQuizQuestion {

    private String question;

    private List<String> options;

    private int correctAnswer;

    private String explanation;


    public AiQuizQuestion() {
    }


    public AiQuizQuestion(
            String question,
            List<String> options,
            int correctAnswer,
            String explanation
    ) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }


    public String getQuestion() {
        return question;
    }


    public void setQuestion(String question) {
        this.question = question;
    }


    public List<String> getOptions() {
        return options;
    }


    public void setOptions(List<String> options) {
        this.options = options;
    }


    public int getCorrectAnswer() {
        return correctAnswer;
    }


    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    public String getExplanation() {
        return explanation;
    }


    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
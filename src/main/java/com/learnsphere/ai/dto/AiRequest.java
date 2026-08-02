package com.learnsphere.ai.dto;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiRequest {

    private Long lessonId;

    private String question;
}
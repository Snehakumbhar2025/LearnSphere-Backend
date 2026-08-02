package com.learnsphere.ai.controller;

import com.learnsphere.ai.dto.AiQuizResponse;
import com.learnsphere.ai.dto.AiRequest;
import com.learnsphere.ai.dto.AiResponse;
import com.learnsphere.ai.service.AiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;


    // =========================================================
    // ===================== CONSTRUCTOR ========================
    // =========================================================

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }


    // =========================================================
    // ================= ASK LEARNSPHERE AI =====================
    // =========================================================

    @PostMapping("/ask")
    public AiResponse askAi(
            @RequestBody AiRequest request
    ) {

        return aiService.askAi(
                request.getLessonId(),
                request.getQuestion()
        );
    }


    // =========================================================
    // ================= GENERATE AI QUIZ =======================
    // =========================================================

    @PostMapping("/quiz/{lessonId}")
    public ResponseEntity<AiQuizResponse> generateQuiz(

            @PathVariable Long lessonId,

            @RequestParam(defaultValue = "5")
            int questions

    ) {

        AiQuizResponse response =
                aiService.generateQuiz(
                        lessonId,
                        questions
                );


        return ResponseEntity.ok(response);
    }
}
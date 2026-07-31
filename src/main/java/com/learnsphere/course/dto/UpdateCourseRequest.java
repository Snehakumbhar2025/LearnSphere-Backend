package com.learnsphere.course.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCourseRequest {

    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String level;
    private String thumbnailUrl;
}
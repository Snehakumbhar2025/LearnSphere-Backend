package com.learnsphere.lesson.entity;



import com.learnsphere.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Lesson title
    @Column(nullable = false)
    private String title;


    // Lesson description/content
    @Column(columnDefinition = "TEXT")
    private String content;


    // Optional video URL
    private String videoUrl;


    // Order of lesson inside the course
    @Column(nullable = false)
    private Integer lessonOrder;


    // Course to which this lesson belongs
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}

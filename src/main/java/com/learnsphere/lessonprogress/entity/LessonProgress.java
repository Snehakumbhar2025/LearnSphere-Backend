package com.learnsphere.lessonprogress.entity;



import com.learnsphere.entity.User;
import com.learnsphere.lesson.entity.Lesson;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "lesson_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "lesson_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Student who completed the lesson
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // Completed lesson
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;


    // Whether lesson is completed
    @Column(nullable = false)
    private boolean completed;


    // Time when lesson was completed
    private LocalDateTime completedAt;
}

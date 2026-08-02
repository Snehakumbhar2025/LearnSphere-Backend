package com.learnsphere.enrollment.entity;


import com.learnsphere.entity.User;
import com.learnsphere.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "course_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Student who enrolled
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // Course selected by student
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    // When student enrolled
    @Column(nullable = false)
    private LocalDateTime enrolledAt;


    // Student progress: 0 - 100
    @Column(nullable = false)
    private Integer progress;


    // Has student completed the course?
    @Column(nullable = false)
    private boolean completed;
}

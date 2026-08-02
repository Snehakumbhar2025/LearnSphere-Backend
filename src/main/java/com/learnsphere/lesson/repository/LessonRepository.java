package com.learnsphere.lesson.repository;



import com.learnsphere.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository
        extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseIdOrderByLessonOrderAsc(Long courseId);
}
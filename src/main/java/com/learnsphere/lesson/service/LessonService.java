package com.learnsphere.lesson.service;


import com.learnsphere.course.entity.Course;
import com.learnsphere.course.service.CourseService;
import com.learnsphere.lesson.entity.Lesson;
import com.learnsphere.lesson.repository.LessonRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseService courseService;

    public LessonService(
            LessonRepository lessonRepository,
            CourseService courseService) {

        this.lessonRepository = lessonRepository;
        this.courseService = courseService;
    }


    // ================= ADD LESSON =================

    public Lesson addLesson(
            Long courseId,
            Lesson lesson) {

        // Find the course
        Course course = courseService.getCourseById(courseId);

        // Connect lesson with course
        lesson.setCourse(course);

        // Save lesson
        return lessonRepository.save(lesson);
    }


    // ================= GET LESSONS BY COURSE =================

    public List<Lesson> getLessonsByCourse(Long courseId) {

        // Make sure course exists
        courseService.getCourseById(courseId);

        // Return lessons in correct order
        return lessonRepository
                .findByCourseIdOrderByLessonOrderAsc(courseId);
    }


    // ================= GET LESSON BY ID =================

    public Lesson getLessonById(Long lessonId) {

        return lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new RuntimeException("Lesson not found"));
    }


    // ================= DELETE LESSON =================

    public void deleteLesson(Long lessonId) {

        Lesson lesson = getLessonById(lessonId);

        lessonRepository.delete(lesson);
    }

    // ================= UPDATE LESSON =================

    public Lesson updateLesson(
            Long lessonId,
            Lesson updatedLesson) {

        // Find existing lesson
        Lesson lesson = getLessonById(lessonId);

        // Update fields
        lesson.setTitle(updatedLesson.getTitle());
        lesson.setContent(updatedLesson.getContent());
        lesson.setVideoUrl(updatedLesson.getVideoUrl());
        lesson.setLessonOrder(updatedLesson.getLessonOrder());

        // Save updated lesson
        return lessonRepository.save(lesson);
    }
}

package com.learnsphere.course.controller;

import com.learnsphere.course.dto.CreateCourseRequest;
import com.learnsphere.course.dto.UpdateCourseRequest;
import com.learnsphere.course.entity.Course;
import com.learnsphere.course.service.CourseService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    // ================= CREATE COURSE =================

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(
            @RequestBody CreateCourseRequest request) {

        return courseService.createCourse(request);
    }


    // ================= GET ALL COURSES =================

    @GetMapping
    public List<Course> getAllCourses() {

        return courseService.getAllCourses();
    }


    // ================= GET COURSE BY ID =================

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {

        return courseService.getCourseById(id);
    }


    // ================= UPDATE COURSE =================

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @PutMapping("/{id}")
    public Course updateCourse(
            @PathVariable Long id,
            @RequestBody UpdateCourseRequest request) {

        return courseService.updateCourse(id, request);
    }


    // ================= DELETE COURSE =================

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {

        courseService.deleteCourse(id);
    }
}
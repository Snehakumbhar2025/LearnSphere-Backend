package com.learnsphere.enrollment.controller;



import com.learnsphere.enrollment.entity.Enrollment;
import com.learnsphere.enrollment.service.EnrollmentService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }


    // ================= ENROLL IN COURSE =================

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{courseId}")
    public Enrollment enrollInCourse(
            @PathVariable Long courseId,
            Authentication authentication) {

        String email = authentication.getName();

        return enrollmentService.enrollStudent(
                email,
                courseId
        );
    }


    // ================= GET MY ENROLLMENTS =================

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my")
    public List<Enrollment> getMyEnrollments(
            Authentication authentication) {

        String email = authentication.getName();

        return enrollmentService.getMyEnrollments(email);
    }
}
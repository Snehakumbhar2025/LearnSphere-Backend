package com.learnsphere.enrollment.service;


import com.learnsphere.course.entity.Course;
import com.learnsphere.course.service.CourseService;
import com.learnsphere.enrollment.entity.Enrollment;
import com.learnsphere.enrollment.repository.EnrollmentRepository;
import com.learnsphere.entity.User;
import com.learnsphere.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository,
            CourseService courseService) {

        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;
    }


    // ================= ENROLL STUDENT =================

    public Enrollment enrollStudent(String email, Long courseId) {

        // Find logged-in student
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // Find course
        Course course = courseService.getCourseById(courseId);


        // Check if already enrolled
        boolean alreadyEnrolled =
                enrollmentRepository.existsByUserIdAndCourseId(
                        user.getId(),
                        courseId
                );


        if (alreadyEnrolled) {

            throw new RuntimeException(
                    "You are already enrolled in this course"
            );
        }


        // Create enrollment
        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .enrolledAt(LocalDateTime.now())
                .progress(0)
                .completed(false)
                .build();


        // Save enrollment in database
        return enrollmentRepository.save(enrollment);
    }


    // ================= GET MY ENROLLMENTS =================

    public List<Enrollment> getMyEnrollments(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return enrollmentRepository.findByUserId(user.getId());
    }
}

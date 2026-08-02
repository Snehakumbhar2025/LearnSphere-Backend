package com.learnsphere.enrollment.repository;


import com.learnsphere.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    // Find all courses enrolled by a particular student
    List<Enrollment> findByUserId(Long userId);

    // Check whether student already enrolled in a course
    Optional<Enrollment> findByUserIdAndCourseId(
            Long userId,
            Long courseId
    );

    // Check quickly if enrollment exists
    boolean existsByUserIdAndCourseId(
            Long userId,
            Long courseId
    );
}

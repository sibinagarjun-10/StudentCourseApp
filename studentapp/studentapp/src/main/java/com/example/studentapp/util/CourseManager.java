package com.example.studentapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppException;
import com.example.studentapp.repository.CourseRepository;

@Component
public class CourseManager {

    private static final Logger log = LogManager.getLogger(CourseManager.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentManager enrollmentManager;

    // ── Lookup ────────────────────────────────────────────────
    public CourseBean findCourseOrThrow(Long courseId) throws AppException {
        try {
            return courseRepository.findById(courseId)
                    .orElseThrow(() -> AppException.courseNotFound(courseId));
        } catch (AppException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("DB error while finding course: {}", courseId, e);
            throw AppException.dbError("Unable to fetch course. Please try again.");
        }
    }

    // ── Validations ───────────────────────────────────────────
    public void checkSeatAvailability(CourseBean course) throws AppException {
        if (course.getAvailableSeats() <= 0) {
            throw AppException.courseFull(course.getName());
        }
    }

    public void checkNotAlreadyEnrolled(Long studentId, Long courseId) throws AppException {
        if (enrollmentManager.isAlreadyEnrolled(studentId, courseId)) {
            throw AppException.alreadyEnrolled(courseId);
        }
    }

    public void checkEnrollmentExists(Long studentId, Long courseId) throws AppException {
        if (!enrollmentManager.isAlreadyEnrolled(studentId, courseId)) {
            throw AppException.enrollmentNotFound(studentId, courseId);
        }
    }

    // ── Seat management ───────────────────────────────────────
    public void decrementSeat(CourseBean course) throws AppException {
        try {
            course.setAvailableSeats(course.getAvailableSeats() - 1);
            courseRepository.save(course);
        } catch (DataAccessException e) {
            log.error("DB error while decrementing seat for course: {}", course.getId(), e);
            throw AppException.dbError("Failed to update seat count. Please try again.");
        }
    }

    public void incrementSeat(CourseBean course) throws AppException {
        try {
            course.setAvailableSeats(course.getAvailableSeats() + 1);
            courseRepository.save(course);
        } catch (DataAccessException e) {
            log.error("DB error while incrementing seat for course: {}", course.getId(), e);
            throw AppException.dbError("Failed to update seat count. Please try again.");
        }
    }
}
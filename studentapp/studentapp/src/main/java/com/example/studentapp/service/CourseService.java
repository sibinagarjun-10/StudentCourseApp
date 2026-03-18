package com.example.studentapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.studentapp.dto.ApiResponse;
import com.example.studentapp.dto.EnrolledCourseResponse;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppException;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.util.CourseManager;
import com.example.studentapp.util.EnrollmentManager;

import java.util.Collections;
import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LogManager.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseManager courseManager;

    @Autowired
    private EnrollmentManager enrollmentManager;

    public List<CourseBean> getAllCourses() throws AppException {
        try {
            log.info("Fetching all courses");
            List<CourseBean> courses = courseRepository.findAll();
            log.debug("Total courses found: {}", courses.size());
            return courses;
        } catch (DataAccessException e) {
            log.error("DB error while fetching all courses", e);
            throw AppException.dbError("Unable to fetch courses. Please try again.");
        }
    }

    public ApiResponse enrollStudent(Long studentId, Long courseId) throws AppException {
        log.info("Enroll request — studentId: {}, courseId: {}", studentId, courseId);
        CourseBean course = courseManager.findCourseOrThrow(courseId);
        courseManager.checkSeatAvailability(course);
        courseManager.checkNotAlreadyEnrolled(studentId, courseId);
        enrollmentManager.saveEnrollment(studentId, courseId);
        courseManager.decrementSeat(course);
        log.info("Student {} enrolled in '{}' successfully", studentId, course.getName());
        return ApiResponse.success("Successfully enrolled in " + course.getName());
    }

    public ApiResponse unEnrollStudent(Long studentId, Long courseId) throws AppException {
        log.info("Unenroll request — studentId: {}, courseId: {}", studentId, courseId);
        courseManager.checkEnrollmentExists(studentId, courseId);
        enrollmentManager.deleteEnrollment(studentId, courseId);
        CourseBean course = courseManager.findCourseOrThrow(courseId);
        courseManager.incrementSeat(course);
        log.info("Student {} unenrolled from '{}' successfully", studentId, course.getName());
        return ApiResponse.success("Successfully unenrolled from " + course.getName());
    }

    public List<EnrolledCourseResponse> getStudentEnrolledCourses(Long studentId) throws AppException {
        try {
            log.info("Fetching enrollments for studentId: {}", studentId);
            List<Long> courseIds = enrollmentManager.getEnrolledCourseIds(studentId);
            log.debug("Enrolled course IDs for studentId {}: {}", studentId, courseIds);
            return courseRepository.findByIdIn(courseIds);
        } catch (AppException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("DB error while fetching enrolled courses for studentId: {}", studentId, e);
            throw AppException.dbError("Unable to fetch enrolled courses. Please try again.");
        }
    }
}
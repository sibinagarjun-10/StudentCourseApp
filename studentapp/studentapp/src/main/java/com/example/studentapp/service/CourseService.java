package com.example.studentapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.studentapp.dto.ApiResponse;
import com.example.studentapp.dto.EnrolledCourseResponse;
import com.example.studentapp.entity.Course;
import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.exception.ConflictException;
import com.example.studentapp.exception.ResourceNotFoundException;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.repository.EnrollmentRepository;
import com.example.studentapp.util.EnrollmentUtil;
import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LogManager.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentUtil enrollmentUtil;

    public List<Course> getAllCourses() {
        log.info("Fetching all courses");
        List<Course> courses = courseRepository.findAll();
        log.debug("Total courses found: {}", courses.size());
        return courses;
    }

    public ApiResponse enrollStudent(Long studentId, Long courseId) {
        log.info("Enroll request — studentId: {}, courseId: {}", studentId, courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Enroll failed — course not found: {}", courseId);
                    return new ResourceNotFoundException(courseId);
                });
        if (course.getAvailableSeats() <= 0) {
            log.warn("Enroll failed — course full: {}", course.getName());
            throw ConflictException.courseFull(course.getName());
        }
        Enrollment existing = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existing != null) {
            log.warn("Enroll failed — student {} already enrolled in course {}", studentId, courseId);
            throw ConflictException.alreadyEnrolled(courseId);
        }
        enrollmentUtil.enrollUtil(studentId, courseId, enrollmentRepository);
        course.setAvailableSeats(course.getAvailableSeats() - 1);
        courseRepository.save(course);
        log.info("Student {} enrolled in '{}' successfully", studentId, course.getName());
        return ApiResponse.success("Successfully enrolled in " + course.getName());
    }

    public ApiResponse unEnrollStudent(Long studentId, Long courseId) {
        log.info("Unenroll request — studentId: {}, courseId: {}", studentId, courseId);
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (enrollment == null) {
            log.warn("Unenroll failed — no enrollment found for studentId: {}, courseId: {}", studentId, courseId);
            throw new ResourceNotFoundException(studentId, courseId);
        }
        enrollmentRepository.delete(enrollment);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    log.warn("Unenroll — course not found after deletion: {}", courseId);
                    return new ResourceNotFoundException(courseId);
                });
        course.setAvailableSeats(course.getAvailableSeats() + 1);
        courseRepository.save(course);
        log.info("Student {} unenrolled from '{}' successfully", studentId, course.getName());
        return ApiResponse.success("Successfully unenrolled from " + course.getName());
    }

    public List<EnrolledCourseResponse> getStudentEnrolledCourses(Long studentId) {
        log.info("Fetching enrollments for studentId: {}", studentId);
        List<Enrollment> myEnrollments = enrollmentRepository.findByStudentId(studentId);
        log.debug("Enrollment count for studentId {}: {}", studentId, myEnrollments.size());
        return myEnrollments.stream()
                .map(enrollment -> courseRepository.findById(enrollment.getCourseId()).orElse(null))
                .filter(course -> course != null)
                .map(course -> new EnrolledCourseResponse(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getDuration()))
                .toList();
    }
}
package com.example.studentapp.util;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.example.studentapp.entity.EnrollmentBean;
import com.example.studentapp.exception.AppException;
import com.example.studentapp.repository.EnrollmentRepository;

@Component
public class EnrollmentManager {

    private static final Logger log = LogManager.getLogger(EnrollmentManager.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public void saveEnrollment(Long studentId, Long courseId) throws AppException {
        try {
            EnrollmentBean enrollment = new EnrollmentBean();
            enrollment.setStudentId(studentId);
            enrollment.setCourseId(courseId);
            enrollment.setEnrolledAt(new Date());
            enrollmentRepository.save(enrollment);
        } catch (DataAccessException e) {
            log.error("DB error while saving enrollment — studentId: {}, courseId: {}", studentId, courseId, e);
            throw AppException.dbError("Failed to save enrollment. Please try again.");
        }
    }

    public boolean isAlreadyEnrolled(Long studentId, Long courseId) throws AppException {
        try {
            return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId) != null;
        } catch (DataAccessException e) {
            log.error("DB error while checking enrollment — studentId: {}, courseId: {}", studentId, courseId, e);
            throw AppException.dbError("Unable to check enrollment status. Please try again.");
        }
    }

    public void deleteEnrollment(Long studentId, Long courseId) throws AppException {
        try {
            EnrollmentBean enrollment =
                enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
            if (enrollment != null) {
                enrollmentRepository.delete(enrollment);
            }
        } catch (DataAccessException e) {
            log.error("DB error while deleting enrollment — studentId: {}, courseId: {}", studentId, courseId, e);
            throw AppException.dbError("Failed to remove enrollment. Please try again.");
        }
    }

    public List<Long> getEnrolledCourseIds(Long studentId) throws AppException {
        try {
            return enrollmentRepository.findByStudentId(studentId)
                       .stream()
                       .map(EnrollmentBean::getCourseId)
                       .toList();
        } catch (DataAccessException e) {
            log.error("DB error while fetching enrolled course IDs for studentId: {}", studentId, e);
            throw AppException.dbError("Unable to fetch enrollments. Please try again.");
        }
    }
}
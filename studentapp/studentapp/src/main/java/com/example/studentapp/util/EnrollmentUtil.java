package com.example.studentapp.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.studentapp.entity.Enrollment;
import com.example.studentapp.repository.EnrollmentRepository;

@Component
public class EnrollmentUtil {
	public static void enrollUtil(Long studentId,Long courseId,EnrollmentRepository enrollmentRepository) {
		Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrolledAt(new Date());
        enrollmentRepository.save(enrollment);

	}
}

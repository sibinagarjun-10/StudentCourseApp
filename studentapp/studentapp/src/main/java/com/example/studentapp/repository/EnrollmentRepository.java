package com.example.studentapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentapp.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
 
	List<Enrollment> findByStudentId(Long studentId);
	
	Enrollment findByStudentIdAndCourseId(Long studentId,Long courseId);
 
}
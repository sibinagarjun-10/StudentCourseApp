package com.example.studentapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentapp.dto.EnrolledCourseResponse;
import com.example.studentapp.entity.CourseBean;

@Repository
public interface CourseRepository extends JpaRepository<CourseBean,Long> {
 
	List<EnrolledCourseResponse> findByIdIn(List<Long> ids);
}
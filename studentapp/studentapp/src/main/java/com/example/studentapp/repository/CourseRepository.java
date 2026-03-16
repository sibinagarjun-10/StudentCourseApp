package com.example.studentapp.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentapp.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
 
        
}
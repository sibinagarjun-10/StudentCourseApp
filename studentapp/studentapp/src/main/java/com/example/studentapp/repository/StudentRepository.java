package com.example.studentapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentapp.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long>{
	Student findByEmail(String email);
}

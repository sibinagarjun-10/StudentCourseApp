package com.example.studentapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentapp.entity.StudentBean;

@Repository
public interface StudentRepository extends JpaRepository<StudentBean,Long>{
	StudentBean findByEmail(String email);
}

package com.example.studentapp.util;

import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.studentapp.dto.RegisterRequest;
import com.example.studentapp.entity.Student;
import com.example.studentapp.repository.StudentRepository;

@Component
public class RegisterUtil {
	public static void registerStudent(RegisterRequest request,BCryptPasswordEncoder encoder,StudentRepository studentRepositpory) {
		Student student = new Student();
	    student.setName(request.getName());
	    student.setEmail(request.getEmail());
	    String hashedPassword = encoder.encode(request.getPassword());
	    student.setPassword(hashedPassword);
	    student.setCreatedAt(new Date());
	    studentRepositpory.save(student);
	}
}

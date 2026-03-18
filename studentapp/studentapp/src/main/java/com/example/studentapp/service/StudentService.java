package com.example.studentapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.studentapp.dto.LoginResponse;
import com.example.studentapp.dto.RegisterRequest;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.exception.AppException;
import com.example.studentapp.util.StudentManager;

@Service
public class StudentService {

    private static final Logger log = LogManager.getLogger(StudentService.class);

    @Autowired
    private StudentManager studentManager;

    public void register(RegisterRequest request) throws AppException {
        log.info("Register request received for email: {}", request.getEmail());
        studentManager.checkEmailNotTaken(request.getEmail());
        studentManager.saveNewStudent(request);
        log.info("Student registered successfully: {}", request.getEmail());
    }

    public LoginResponse login(String email, String password) throws AppException {
        log.info("Login attempt for email: {}", email);
        StudentBean student = studentManager.findStudentByEmailOrThrow(email);
        studentManager.verifyPasswordOrThrow(password, student);
        log.info("Login successful for email: {}", email);
        return new LoginResponse(student.getId(), student.getName(), "Login successful");
    }
}
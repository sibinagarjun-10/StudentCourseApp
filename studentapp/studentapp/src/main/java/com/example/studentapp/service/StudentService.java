package com.example.studentapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.studentapp.dto.LoginResponse;
import com.example.studentapp.dto.RegisterRequest;
import com.example.studentapp.entity.Student;
import com.example.studentapp.exception.ConflictException;
import com.example.studentapp.exception.InvalidCredentialsException;
import com.example.studentapp.exception.ResourceNotFoundException;
import com.example.studentapp.repository.StudentRepository;
import com.example.studentapp.util.RegisterUtil;

@Service
public class StudentService {

    private static final Logger log = LogManager.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepositpory;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private RegisterUtil registerUtil;

    public void register(RegisterRequest request) {
        log.info("Register request received for email: {}", request.getEmail());
        Student existing = studentRepositpory.findByEmail(request.getEmail());
        if (existing != null) {
            log.warn("Registration failed — email already exists: {}", request.getEmail());
            throw ConflictException.emailAlreadyRegistered(request.getEmail());
        }
        registerUtil.registerStudent(request, encoder, studentRepositpory);
        log.info("Student registered successfully: {}", request.getEmail());
    }

    public LoginResponse login(String email, String password) {
        log.info("Login attempt for email: {}", email);
        Student student = studentRepositpory.findByEmail(email);
        if (student == null) {
            log.warn("Login failed — no account found for email: {}", email);
            throw new ResourceNotFoundException(email);
        }
        if (!encoder.matches(password, student.getPassword())) {
            log.warn("Login failed — invalid password for email: {}", email);
            throw new InvalidCredentialsException();
        }
        log.info("Login successful for email: {}", email);
        return new LoginResponse(student.getId(), student.getName(), "Login successful");
    }
}
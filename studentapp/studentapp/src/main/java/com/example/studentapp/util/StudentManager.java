package com.example.studentapp.util;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.studentapp.dto.RegisterRequest;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.exception.AppException;
import com.example.studentapp.repository.StudentRepository;

@Component
public class StudentManager {

    private static final Logger log = LogManager.getLogger(StudentManager.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // ── Save ──────────────────────────────────────────────────
    public void saveNewStudent(RegisterRequest request) throws AppException {
        try {
            StudentBean student = new StudentBean();
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            student.setPassword(encoder.encode(request.getPassword()));
            student.setCreatedAt(new Date());
            studentRepository.save(student);
        } catch (DataAccessException e) {
            log.error("DB error while saving student for email: {}", request.getEmail(), e);
            throw AppException.dbError("Failed to save student. Please try again.");
        }
    }

    // ── Validations ───────────────────────────────────────────
    public void checkEmailNotTaken(String email) throws AppException {
        try {
            if (studentRepository.findByEmail(email) != null) {
                throw AppException.emailAlreadyRegistered(email);
            }
        } catch (AppException e) {
            throw e; 
        } catch (DataAccessException e) {
            log.error("DB error while checking email: {}", email, e);
            throw AppException.dbError("Unable to verify email. Please try again.");
        }
    }

    public StudentBean findStudentByEmailOrThrow(String email) throws AppException {
        try {
            StudentBean student = studentRepository.findByEmail(email);
            if (student == null) {
                throw AppException.emailNotFound(email);
            }
            return student;
        } catch (AppException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("DB error while finding student for email: {}", email, e);
            throw AppException.dbError("Unable to find account. Please try again.");
        }
    }

    public void verifyPasswordOrThrow(String rawPassword, StudentBean student) throws AppException {
        try {
            if (!encoder.matches(rawPassword, student.getPassword())) {
                throw AppException.invalidCredentials();
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during password verification for email: {}", student.getEmail(), e);
            throw AppException.dbError("Unable to verify credentials. Please try again.");
        }
    }
}
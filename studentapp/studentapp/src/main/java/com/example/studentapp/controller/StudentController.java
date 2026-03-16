package com.example.studentapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentapp.dto.ApiResponse;
import com.example.studentapp.dto.EnrolledCourseResponse;
import com.example.studentapp.dto.LoginRequest;
import com.example.studentapp.dto.LoginResponse;
import com.example.studentapp.dto.RegisterRequest;
import com.example.studentapp.service.CourseService;
import com.example.studentapp.service.StudentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    // ── Register ─────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        studentService.register(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Registration successful"));
    }

    // ── Login: returns JSON + sets server-side session ────────
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                               HttpSession session) {
        LoginResponse resp = studentService.login(loginRequest.getEmail(), loginRequest.getPassword());
        session.setAttribute("studentId", resp.getStudentId());   // server session
        session.setAttribute("studentName", resp.getName());       // server session
        return ResponseEntity.ok(resp);
    }

    // ── Logout: invalidates server session ───────────────────
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    // ── Get enrolled courses for a student ───────────────────
    @GetMapping("/{studentId}/enrollments")
    public ResponseEntity<List<EnrolledCourseResponse>> getMyEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.getStudentEnrolledCourses(studentId));
    }
}
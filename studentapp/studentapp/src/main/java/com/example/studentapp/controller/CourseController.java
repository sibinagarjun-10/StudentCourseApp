package com.example.studentapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentapp.dto.ApiResponse;
import com.example.studentapp.dto.EnrollmentRequest;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppException;
import com.example.studentapp.service.CourseService;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // ── Get all courses ───────────────────────────────────────
    @GetMapping("/courses")
    public ResponseEntity<List<CourseBean>> getAllCourses() throws AppException {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // ── Enroll ────────────────────────────────────────────────
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<ApiResponse> enroll(@PathVariable Long courseId,
                                              @RequestBody EnrollmentRequest enrollmentRequest) throws AppException {
        return ResponseEntity.ok(courseService.enrollStudent(enrollmentRequest.getStudentId(), courseId));
    }

    // ── Unenroll ──────────────────────────────────────────────
    @DeleteMapping("/enroll/{courseId}")
    public ResponseEntity<ApiResponse> unEnroll(@PathVariable Long courseId,
                                                @RequestBody EnrollmentRequest enrollmentRequest) throws AppException {
        return ResponseEntity.ok(courseService.unEnrollStudent(enrollmentRequest.getStudentId(), courseId));
    }
}
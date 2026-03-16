package com.example.studentapp.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String title;

    public ResourceNotFoundException(String email) {
        super("No account found with email: " + email);
        this.title = "Not Found";
    }

    public ResourceNotFoundException(Long id) {
        super("Course not found with ID: " + id);
        this.title = "Not Found";
    }

    public ResourceNotFoundException(Long studentId, Long courseId) {
        super("No enrollment found for student ID " + studentId + " in course ID " + courseId);
        this.title = "Enrollment Not Found";
    }

    public String getTitle() { return title; }
}
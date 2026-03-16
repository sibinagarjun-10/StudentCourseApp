package com.example.studentapp.exception;

public class ConflictException extends RuntimeException {
    private final String title;

    private ConflictException(String title, String message) {
        super(message);
        this.title = title;
    }

    public static ConflictException emailAlreadyRegistered(String email) {
        return new ConflictException("Email Already Registered",
            "Email " + email + " is already registered");
    }

    public static ConflictException courseFull(String courseName) {
        return new ConflictException("Course Full",
            "No seats available for course: " + courseName);
    }

    public static ConflictException alreadyEnrolled(Long courseId) {
        return new ConflictException("Already Enrolled",
            "You are already enrolled in course with ID: " + courseId);
    }

    public String getTitle() { return title; }
}
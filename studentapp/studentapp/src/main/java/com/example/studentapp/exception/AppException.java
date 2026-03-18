package com.example.studentapp.exception;

public class AppException extends Exception {

    private static final long serialVersionUID = 1L;

    public enum Type {
        CONFLICT,
        NOT_FOUND,
        INVALID_CREDENTIALS,
        DB_ERROR 
    }

    private final Type type;
    private final String title;

    private AppException(Type type, String title, String message) {
        super(message);
        this.type = type;
        this.title = title;
    }

    // ── ConflictException replacements ────────────────────────
    public static AppException emailAlreadyRegistered(String email) {
        return new AppException(Type.CONFLICT, "Email Already Registered",
            "Email " + email + " is already registered");
    }

    public static AppException courseFull(String courseName) {
        return new AppException(Type.CONFLICT, "Course Full",
            "No seats available for course: " + courseName);
    }

    public static AppException alreadyEnrolled(Long courseId) {
        return new AppException(Type.CONFLICT, "Already Enrolled",
            "You are already enrolled in course with ID: " + courseId);
    }

    // ── ResourceNotFoundException replacements ────────────────
    public static AppException emailNotFound(String email) {
        return new AppException(Type.NOT_FOUND, "Account Not Found",
            "No account found with email: " + email);
    }

    public static AppException courseNotFound(Long courseId) {
        return new AppException(Type.NOT_FOUND, "Course Not Found",
            "Course not found with ID: " + courseId);
    }

    public static AppException enrollmentNotFound(Long studentId, Long courseId) {
        return new AppException(Type.NOT_FOUND, "Enrollment Not Found",
            "No enrollment found for student ID " + studentId + " in course ID " + courseId);
    }

    // ── InvalidCredentialsException replacement ───────────────
    public static AppException invalidCredentials() {
        return new AppException(Type.INVALID_CREDENTIALS, "Invalid Credentials",
            "Invalid email or password");
    }
    
    public static AppException dbError(String message) {
        return new AppException(Type.DB_ERROR, "Database Error", message);
    }

    public Type getType()   { return type; }
    public String getTitle(){ return title; }
}
package com.example.studentapp.dto;

public class EnrolledCourseResponse {

    private Long courseId;
    private String courseName;
    private String description;
    private String duration;

    public EnrolledCourseResponse() {}

    public EnrolledCourseResponse(Long courseId, String courseName, String description, String duration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.duration = duration;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }
}
package com.example.studentapp.dto;

public class LoginResponse {
	private String status;
    private String message;
    private Long studentId;
    private String name;
 
    public LoginResponse(Long studentId, String name, String message) {
        this.status = "success";
        this.message = message;
        this.studentId = studentId;
        this.name = name;
    }
 
    public String getStatus()    { return status; }
    public String getMessage()   { return message; }
    public Long getStudentId()   { return studentId; }
    public String getName()      { return name; }
}
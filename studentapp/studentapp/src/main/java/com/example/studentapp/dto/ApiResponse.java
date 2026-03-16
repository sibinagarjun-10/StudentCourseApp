package com.example.studentapp.dto;

public class ApiResponse {
	private String status;
    private String message;
 
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
 
    public static ApiResponse success(String message) {
        return new ApiResponse("success", message);
    }
 
    public static ApiResponse error(String message) {
        return new ApiResponse("error", message);
    }
 
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}

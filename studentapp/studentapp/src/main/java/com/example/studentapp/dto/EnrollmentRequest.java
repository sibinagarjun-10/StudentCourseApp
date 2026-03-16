package com.example.studentapp.dto;

import jakarta.validation.constraints.NotNull;

public class EnrollmentRequest {
	
	@NotNull(message="Student Id is required for enrollment process")
	private Long studentId;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
	
	
}

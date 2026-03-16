package com.example.studentapp.entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	    name = "ENROLLMENT",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"STUDENT_ID", "COURSE_ID"})
	    }
	)
public class Enrollment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "STUDENT_ID")
	private Long studentId;
	
	@Column(name = "COURSE_ID")
	private Long courseId;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENROLLED_AT")
	private Date enrolledAt;
	
	public Enrollment() {}

	public Enrollment(Long id, Long studentId, Long courseId, Date enrolledAt) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.courseId = courseId;
		this.enrolledAt = enrolledAt;
	}

	public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Date getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(Date enrolledAt) { this.enrolledAt = enrolledAt; }

	@Override
	public String toString() {
		return "Enrollment [id=" + id + ", studentId=" + studentId + ", courseId=" + courseId + ", enrolledAt="
				+ enrolledAt + "]";
	}
	
	
	

}

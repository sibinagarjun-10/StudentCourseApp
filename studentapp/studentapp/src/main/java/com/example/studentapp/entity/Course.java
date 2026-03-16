package com.example.studentapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "COURSE")
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NAME", nullable = false, length = 100)
	private String name;
	
	@Column(name = "DESCRIPTION", length = 500)
	private String description;
	
	@Column(name = "DURATION", length = 50)
	private String duration;
	
	@Column(name = "TOTAL_SEATS")
	private int totalSeats;
	
	@Column(name = "AVAILABLE_SEATS")
	private int availableSeats;
	
	public Course() {}

	public Course(Long id, String name, String description, String duration, int totalSeats, int availableSeats) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.duration = duration;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", description=" + description + ", duration=" + duration
				+ ", totalSeats=" + totalSeats + ", availableSeats=" + availableSeats + "]";
	}
	
	

}

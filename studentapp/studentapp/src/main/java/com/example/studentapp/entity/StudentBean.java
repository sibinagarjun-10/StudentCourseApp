package com.example.studentapp.entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "STUDENT")
public class StudentBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NAME", nullable = false, length = 100)
	private String name;
	
	@Column(name = "EMAIL", nullable = false, unique = true, length = 100)
	private String email;
	
	
	@Column(name = "PASSWORD", nullable = false, length = 100)
	private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
	private Date createdAt;
	
	public StudentBean() {}
	public StudentBean(Long id, String name, String email, String password, Date createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", createdAt="
				+ createdAt + "]";
	}
	
	
	

}
